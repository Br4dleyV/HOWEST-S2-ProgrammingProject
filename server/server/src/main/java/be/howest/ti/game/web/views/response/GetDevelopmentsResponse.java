package be.howest.ti.game.web.views.response;

import java.util.List;
import be.howest.ti.game.logic.classes.Card;
import be.howest.ti.game.logic.classes.DataReader;

public class GetDevelopmentsResponse extends AbstractResponseWithHiddenStatus {
    public GetDevelopmentsResponse() {
        super(200);
    }

    public List<Card> getDevelopments() {
        return DataReader.readDevelopmentCards("/data/developments.txt");
    }
}