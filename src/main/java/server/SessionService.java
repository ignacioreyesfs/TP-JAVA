package server;

import spark.Request;

public class SessionService {
    private static final String UUID = "uuid";

    public static Integer getSessionId(Request req) {
        return req.session().attribute(UUID);
    }

    public static void setSessionId(Request req, Integer uuid) {
        req.session().attribute(UUID, uuid);
    }

    public static void removeSessionId(Request req) { req.session().removeAttribute(UUID); }
}
