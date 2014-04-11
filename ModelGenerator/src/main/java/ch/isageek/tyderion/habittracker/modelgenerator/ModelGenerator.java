package ch.isageek.tyderion.habittracker.modelgenerator;

import de.greenrobot.daogenerator.Annotation;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class ModelGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "ch.isageek.tyderion.habittracker.model");
        schema.enableKeepSectionsByDefault();


        Entity habit = schema.addEntity("Habit");
        habit.addIdProperty();
        habit.addDateProperty("dateCreated");
        habit.addStringProperty("name").unique();
        habit.addBooleanProperty("isPositive");

        Entity occurence = schema.addEntity("Occurence");
        occurence.addIdProperty();
        occurence.addDateProperty("date");
        Property habitID = occurence.addLongProperty("habitID").getProperty();
        occurence.addToOne(habit, habitID);
        occurence.addToMany(habit, habitID);

        new DaoGenerator().generateAll(schema, "src-gen/main/java");
    }
}