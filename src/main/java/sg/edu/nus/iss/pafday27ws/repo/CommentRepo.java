package sg.edu.nus.iss.pafday27ws.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import sg.edu.nus.iss.pafday27ws.model.Comment;

@Repository
public class CommentRepo {
    @Autowired
    private MongoTemplate template;

    private final String G_GID = "gid";
    private final String C_COMMENT = "comment";
    private final String G_NAME = "name";
    private final String G_GAME = "game";

    // public Boolean checkIfGameExists(int gid) {
    // Criteria c = Criteria.where(C_GID).is(gid);
    // Query query = Query.query(c);

    // return template.exists(query, G_GAME);

    // }
    public Boolean checkIfGameExists(int gid) {
        return template.exists(Query.query(Criteria.where(G_GID).is(gid)), G_GAME);
    }

    public String getGameNameById(int gid) {
        Criteria c = Criteria.where(G_GID).is(gid);
        Query query = Query.query(c);

        return template.findOne(query, Document.class, G_GAME).getString(G_NAME);

    }

    public Boolean checkCommentExists(String reviewId) {
        Criteria c = Criteria.where("_id").is(reviewId);
        Query query = Query.query(c);

        return template.exists(query, C_COMMENT);

    }
    /*
     * db.comment.updateOne(
     * { c_id: "32d02833-adad-4980-a5df-a45e526af5e0" },
     * {
     * $push: {
     * edited: {
     * comment: "testing update",
     * rating: "testing rating",
     * posted: "date"
     * }
     * }
     * }
     * )
     */
    // public Boolean updateComment(String reviewId, Comment update) {

    // Document retrievedDoc =
    // template.findOne(Query.query(Criteria.where("_id").is(reviewId)),
    // Document.class, C_COMMENT );
    // Date date = retrievedDoc.getDate("posted");

    // Document updated = new Document()
    // .append("comment", update.getComment())
    // .append("rating", update.getRating())
    // .append("posted", date);

    // if(retrievedDoc.getList("edited", Document.class) == null) {
    // List<Document> list = new ArrayList<>();
    // list.add(updated);
    // retrievedDoc.append("edited", list);
    // } else {
    // List<Document> editedList = retrievedDoc.getList("edited", Document.class);
    // editedList.add(updated);
    // retrievedDoc.put("edited", editedList);

    // }
    // retrievedDoc.put("posted", new Date());

    // template.save(retrievedDoc, C_COMMENT);

    // return true;
    public Boolean updateComment(String reviewId, Comment update) {
        Query query = Query.query(Criteria.where("_id").is(reviewId));
        Document retrievedDoc = template.findOne(query, Document.class, C_COMMENT);

        Date date = retrievedDoc.getDate("posted");

        Document editedComment = new Document()
                .append("comment", update.getComment())
                .append("rating", update.getRating())
                .append("posted", new Date());

        Update updates = new Update()
                .push("edited", editedComment)
                .set("posted", date);

        UpdateResult result = template.updateFirst(query, updates, C_COMMENT);

        return result.wasAcknowledged();
    }

    // Update updates = new Update()
    // .push("edited", updated)
    // .set("posted", new Date());

    // Criteria c = Criteria.where("_id").is(reviewId);
    // Query query = Query.query(c);

    // UpdateResult result = template.updateFirst(query, updates, Comment.class);

    // return result.wasAcknowledged();
    // }

    public Document getUpdatedComments(String reviewId) {
        return template.findOne(Query.query(Criteria.where("_id").is(reviewId)), Document.class, C_COMMENT);
    }

    public Document newComment(Document d) {
        return template.insert(d, "comment");
    }

}
