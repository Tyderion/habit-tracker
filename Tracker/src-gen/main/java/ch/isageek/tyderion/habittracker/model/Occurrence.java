package ch.isageek.tyderion.habittracker.model;



// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import java.text.SimpleDateFormat;
// KEEP INCLUDES END
/**
 * Entity mapped to table OCCURRENCE.
 */
public class Occurrence extends OccurrenceBase  {
    // KEEP FIELDS - put your custom fields here
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    // KEEP FIELDS END
    public Occurrence() {
    }

    public Occurrence(Long id) {
        super(id);
    }

    public Occurrence(Long id, java.util.Date date, Long habitID) {
        super(id, date, habitID);
    }

    // KEEP METHODS - put your custom methods here
    public Occurrence(java.util.Date date, Long habitID) {
        super(null, date, habitID);
    }

    @Override
    public String toString() {
        return "Occurrence at " + Occurrence.dateFormatter.format(this.date);
    }
    // KEEP METHODS END

}
