package upgrade.ntv.bangsoccer.Utils;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.Attraction.Area;

/**
 * Json Writer
 * <p>
 * Created by Paulino Gomez on 11/13/2015.
 */
public class JsonWriter {

    public void writeJsonStream(OutputStream out, ArrayList<Area> areasList) throws IOException {
        android.util.JsonWriter writer = new android.util.JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeAreasArray(writer, areasList);
        writer.close();
    }

    private void writeAreasArray(android.util.JsonWriter writer, List<Area> areasList) throws IOException {
        writer.beginArray();
        for (Area area : areasList) {
            writeArea(writer, area);
        }
        writer.endArray();
    }

    private void writeArea(android.util.JsonWriter writer, Area area) throws IOException {
        writer.beginObject();

        writer.name("Id").value(area.getId());
        writer.name("Area_Name").value(area.getName());

        writer.name("geo");
        writeGeoLocation(writer, area.getGeo());

        writer.name("Area_Type").value(area.getType());

        writer.endObject();
    }

    private void writeGeoLocation(android.util.JsonWriter writer, LatLng latLng) throws IOException {
        writer.beginArray();
        writer.value(latLng.latitude);
        writer.value(latLng.longitude);
        writer.endArray();
    }

}
