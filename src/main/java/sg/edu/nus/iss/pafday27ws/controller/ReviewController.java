package sg.edu.nus.iss.pafday27ws.controller;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.pafday27ws.model.Comment;
import sg.edu.nus.iss.pafday27ws.service.CommentService;

@RestController
@RequestMapping
public class ReviewController {

    @Autowired
    private CommentService cSvc;

    @PostMapping(path = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newReview(@RequestBody Comment c) {
        
        if (cSvc.checkIfGameExists(c.getGid())) {
            JsonObject obj = Json.createObjectBuilder()
            //concat Document object with "<message>", implicitly passing it as a string representation to be used as JSON value
            .add("success", "Successfully posted with ID " + cSvc.newComment(c).getObjectId("_id") + ".")
            .build();
            //print out ----> {"success": "Successfully posted with ID 64b2e193d887321c3db67fa7."} 
              /* 
            .add("success", "Comment added").build();
             print out: ------>   {"success": "Comment added"}   
            .add("success", " "+ cSvc.newComment(c)).build();
             print out ---->"success": " Document{{user=anne, rating=5, comment=testing, gid=2, posted=Sun Jul 16 02:09:47 SGT 2023, name=Dragonmaster, _id=64b2e0ecd887321c3db67f9b}}"
            */
            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK); 

        } else {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", "No game with ID " + c.getGid() + " found")
                    .build();
            return new ResponseEntity<String>(error.toString(), HttpStatus.NOT_FOUND);

        }
    }

    @PutMapping(path="review/{reviewId}") 
        public ResponseEntity<String> updateComment(@PathVariable String reviewId, @RequestBody Comment update) {

            if (cSvc.updateComment(reviewId, update)) {
            JsonObject obj = Json.createObjectBuilder()
                    .add("success", "Review updated successfully.")
                    .build();
            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
        } else {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", "Error")
                    .build();

            return new ResponseEntity<String>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

    @GetMapping("/review/{reviewId}")    
    public ResponseEntity<String> displayComment(@PathVariable String reviewId) {
        Document comments = cSvc.getUpdatedComments(reviewId);

        if(comments != null) {            
            JsonObject obj = Json.createObjectBuilder()
                .add("user", "" + comments.getString("user"))
                .add("rating", "" + comments.getInteger("rating"))
                .add("comment", "" + comments.getString("comment"))
                .add("ID", "" + comments.getObjectId("_id"))
                .add("posted", "" + comments.getDate("posted"))
                .add("name", "" + comments.getString("name"))
                .add("edited", cSvc.checkReviewEdited(comments))
                .add("timestamp", "" + new Date())
                .build();

            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
        } else {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", "Error")
                    .build();

            return new ResponseEntity<String>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}    
   
    
//     @PostMapping(path = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<String> addComment(Comment comment) {
//          if (cSvc.checkIfGameExists(comment.getGid())) {
//              cSvc.newComment(comment);
//              return ResponseEntity.status(HttpStatus.OK).build();
//          } else {
//              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"No game with ID " + comment.getGid() + " found\"}");
//      }
// }

    