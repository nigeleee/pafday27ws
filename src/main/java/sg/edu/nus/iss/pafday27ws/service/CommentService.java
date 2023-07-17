package sg.edu.nus.iss.pafday27ws.service;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import sg.edu.nus.iss.pafday27ws.model.Comment;
import sg.edu.nus.iss.pafday27ws.repo.CommentRepo;

@Repository
public class CommentService {
    @Autowired
    private CommentRepo cRepo;

    // public List<Comment> findCommentsByGid(int gid) {
    //     List<Comment> commentList = cRepo.getCommentByGid(gid);

    //     if(commentList.isEmpty()) {
    //         throw new NoSuchElementException("Game gid " + gid + " does not exist");
    //     } 
        
    //     return commentList;

    // }
    
    public Boolean checkIfGameExists(int gid) {
        return cRepo.checkIfGameExists(gid);
    
    }


    // public Document newComment(Comment c) {
    //     JsonObject obj = Json.createObjectBuilder()
    //         .add("user", c.getUser())
    //         .add("rating", c.getRating())
    //         .add("comment", c.getComment())
    //         .add("gid", c.getGid())
    //         .build();
    //     return cRepo.newComment(Document.parse(obj.toString()));
          
    // }

    public Document newComment(Comment c) {

        Document d = new Document();
            d.append("user", c.getUser());
            d.append("rating", c.getRating());
            d.append("comment", c.getComment());
            d.append("gid", c.getGid());
            d.append("posted", new Date());
            d.append("name", cRepo.getGameNameById(c.getGid()));

        return cRepo.newComment(d);
    }

    public Boolean updateComment(String reviewId, Comment update) { 

        return cRepo.updateComment(reviewId, update);
    }

    public Document getUpdatedComments(String reviewId) {
        return cRepo.getUpdatedComments(reviewId);
    }

    public Boolean checkReviewEdited (Document edited) {
        List<Document> edit = edited.getList("edited", Document.class);
        System.out.println("------------>" + edit);
        if (edit.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}

