package be.howest.ti.game.web.views.response;

public class GetInfoResponse extends AbstractResponseWithHiddenStatus {
    private static  final  String NAME = "Splendor API";
    private static final String VERSION = "1.0.0";
    private static final String DESCRIPTION = "API for Splendor game";

    public GetInfoResponse() {
        super(200);
    }

    public String getName() { return NAME; }
    public String getVersion() { return VERSION; }
    public String getDescription() { return DESCRIPTION; }
}