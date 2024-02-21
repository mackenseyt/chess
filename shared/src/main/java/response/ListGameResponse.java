package response;
import java.util.ArrayList;

public record ListGameResponse(ArrayList<model.GameData> games) {
}
