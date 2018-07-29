package mongo_db;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

import com.mongodb.MongoClient;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;


public class MongoService {
    MongoClient client = new MongoClient("localhost", 27017); //connect to mongodb
    Datastore datastore = new Morphia().createDatastore(client, "data_test"); //select shop collection

    public void clear() {
        datastore.getCollection(Marker.class).drop();
        datastore.getCollection(Route.class).drop();
        datastore.getCollection(Comment.class).drop();
    }

    public String addMarker(Marker marker) {
        datastore.save(marker);
        return marker.getId();
    }

    public String addRoute(Route route) {
        datastore.save(route);
        return route.getId();
    }

    public String addComment(Comment comment) {
        datastore.save(comment);
        return comment.getId();
    }

    public List<Marker> getAllMarkers() {
        List<Marker> list = datastore.find(Marker.class).asList();
        if (list != null) {
            return list;
        }
        return null;
    }

    public List<Route> getAllRoutes() {
        List<Route> list = datastore.find(Route.class).asList();
        if (list != null) {
            return list;
        }
        return null;
    }

    public List<Marker> getMarkers(boolean isGetNotOwned, Long userID) {
        List<Marker> list = null;

        System.out.println("Markers");
        if (isGetNotOwned) {
            System.out.println("get others");
            list = datastore.createQuery(Marker.class).field("ownerID").notEqual(userID)
                        .field("isPublic").equal(Boolean.TRUE).asList();
            System.out.println(list.size());
        }
        list.addAll(datastore.createQuery(Marker.class).field("ownerID").equal(userID).asList());
        System.out.println(list.size());
        return list;
    }

    public List<Comment> getComments(String markerId){
        List<Comment> list = null;

        list.addAll(datastore.createQuery(Comment.class).field("markerId").equal(markerId).asList());
        return list;
    }




    public List<Route> getRoutes(boolean isGetNotOwned, Long userID) {
        List<Route> list = null;

        if (isGetNotOwned) {
             list = datastore.createQuery(Route.class).field("ownerID").notEqual(userID)
                    .field("isPublic").equal(true).asList();
        }
        list.addAll(datastore.createQuery(Route.class).field("ownerID").equal(userID).asList());

        return list;
    }

    public void changeStatus(String id, Boolean isPublic) {
        Query<Marker> query = datastore.createQuery(Marker.class).field("_id").equal(new ObjectId(id));
        UpdateOperations<Marker> ops = datastore.createUpdateOperations(Marker.class).set("isPublic", isPublic);

        System.out.println(datastore.update(query, ops).getUpdatedCount());
    }


}
