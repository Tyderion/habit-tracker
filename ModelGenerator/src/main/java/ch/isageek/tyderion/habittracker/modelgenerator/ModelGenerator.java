package ch.isageek.tyderion.habittracker.modelgenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class ModelGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(5, "ch.isageek.tyderion.habittracker.model");
        schema.enableKeepSectionsByDefault();


        Entity habit = schema.addEntity("Habit");
        habit.addIdProperty();
        habit.addDateProperty("dateCreated");
        habit.addStringProperty("name").unique();
        habit.addBooleanProperty("isPositive");
        habit.addStringProperty("description");
        habit.addStringProperty("uuid");

        Entity occurrence = schema.addEntity("Occurrence");
        occurrence.addIdProperty();
        Property dateProperty = occurrence.addDateProperty("date").getProperty();
        Property habitID = occurrence.addLongProperty("habitID").getProperty();
        occurrence.addToOne(habit, habitID);
        ToMany occurencesToMany = habit.addToMany(occurrence, habitID);
        occurencesToMany.orderDesc(dateProperty);
//        occurence.addToMany(habit, habitID);

        new DaoGenerator().generateAll(schema, "src-gen/main/java");
    }
}