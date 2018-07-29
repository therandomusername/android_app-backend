package mongo_db;

import static spark.Spark.*;

import com.google.gson.Gson;
import java.util.concurrent.atomic.AtomicBoolean;

public class Api {

    private static MongoService mongo = new MongoService();
    private static Gson gsonTransformer = new Gson();


    public static boolean verify(String token){
        AtomicBoolean isVerified = new AtomicBoolean(false);
        get("graph.facebook.com/debug_token?input_token={\"+token+\"}&access_token={1661504553926840}", (req, res) -> {
            res.type("application/json");
            if(res.body().contains("error")) isVerified.set(false);
            else isVerified.set(true);
            return res;
        }, gsonTransformer::toJson);
        return isVerified.get();
    }





    public static void main(String[] args) {



        post("/addSomeData", (req, res) -> {
            mongo.clear();

            Marker marker = new Marker(51.1222822, 17.060590, "Nice place!", "opis_0", true, 1L);
            mongo.addMarker(marker);

            marker = new Marker(51.1845689, 51.1845689, "Big tree!", "opis_1", false, 1L);
            mongo.addMarker(marker);

            marker = new Marker(51.18415689, 17.0545594, "I like this!", "opis_2", true, 1L);
            mongo.addMarker(marker);

            marker = new Marker(51.18145689, 17.0514594, "Best shop on Citadel!", "opis_3", true, 1L);
            mongo.addMarker(marker);

            marker = new Marker(51.18172689, 17.0514524, "Come here", "opis_4", false, 1L);
            mongo.addMarker(marker);

            Coordinates[] temp = {new Coordinates(51.18112689, 17.0511524), new Coordinates(51.18472689, 17.0714524)};
            Route route = new Route(temp, "That's amazing!", "opis_0", false,1L);
            mongo.addRoute(route);

            Coordinates[] temp1 = {new Coordinates(51.14112689, 17.0141524), new Coordinates(51.18485689, 17.0772524)};
            route = new Route(temp1, "Nice walk!", "opis_1", false, 1L);
            mongo.addRoute(route);

            return "example data added";
        }, gsonTransformer::toJson);

        post("/addMarker", (req, res) -> {
            if(verify(req.queryParams("ACCESS-TOKEN"))) {
                res.type("application/json");
                Marker marker = gsonTransformer.fromJson(req.body(), Marker.class);
                return mongo.addMarker(marker);
            } else {
                return "error-wrong-token";
            }
        }, gsonTransformer::toJson);

        post("/addRoute", (req, res) -> {
            if(verify(req.queryParams("ACCESS-TOKEN"))) {
            res.type("application/json");
            Route route = gsonTransformer.fromJson(req.body(), Route.class);
            return mongo.addRoute(route);
            } else {
                return "error-wrong-token";
            }
        }, gsonTransformer::toJson);

        post("/addComment", (req, res) -> {
            if(verify(req.queryParams("ACCESS-TOKEN"))) {
                res.type("application/json");
                Comment comment = gsonTransformer.fromJson(req.body(), Comment.class);
                return mongo.addComment(comment);
            } else {
                return "error-wrong-token";
            }
        }, gsonTransformer::toJson);

        get("/getAllMarkers", (req, res) -> {
            res.type("application/json");
            return mongo.getAllMarkers();
        }, gsonTransformer::toJson);


        get("/getAllRoutes", (req, res) -> {
            res.type("application/json");
            return mongo.getAllRoutes();
        }, gsonTransformer::toJson);

        get("/getMarkers", (req, res) -> {
            System.out.println(req.queryParams("getOthers"));
            System.out.println(req.queryParams("userID"));
            boolean owned = Boolean.valueOf(req.queryParams("getOthers"));
            Long userID = Long.valueOf(req.queryParams("userID"));
            res.type("application/json");

            return mongo.getMarkers(owned, userID);
        }, gsonTransformer::toJson);

        get("/getComments", (req, res) -> {
            System.out.println(req.queryParams("markerId"));
            String markerId =  gsonTransformer.fromJson(req.body(), String.class);
            res.type("application/json");
            return mongo.getComments(markerId);
        }, gsonTransformer::toJson);

        get("/getRoutes", (req, res) -> {
            boolean owned = Boolean.valueOf(req.queryParams("getOthers"));
            Long userID = Long.valueOf(req.queryParams("userID"));
            res.type("application/json");

            return mongo.getRoutes(owned, userID);
        }, gsonTransformer::toJson);

        put("/changeStatus", (req, res) -> {
            System.out.println(req.body());
            ChangeStatusReqBody body = gsonTransformer.fromJson(req.body(), ChangeStatusReqBody.class);
            mongo.changeStatus(body.getId(), body.isPublic());
            return "";
        }, gsonTransformer::toJson);
    }
}
