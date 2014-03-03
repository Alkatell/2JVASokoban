package com.supinfo.sokoban;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Score
{
    private long startedAt;
    private long stoppedAt;
    private boolean timerOn;
    private int moves;
    public static final String PATH = "scores.json";

    public Score()
    {
        this.startedAt = 0;
        this.stoppedAt = 0;
        this.timerOn = false;
        this.moves = 0;
    }

    public void startTimer()
    {
        this.startedAt = new Date().getTime();
        this.timerOn = true;
    }

    public void stopTimer()
    {
        this.stoppedAt = new Date().getTime();
        this.timerOn = false;
    }

    public String getResult()
    {
        long difference = this.stoppedAt - this.startedAt;

        long minutes = difference / (60 * 1000) % 60;
        long seconds = difference / 1000 % 60;
        long milliseconds = difference % 60;

        String result = this.moves + " mouvements en ";

        if(minutes == 1)
        {
            result += "1 minute ";
        }

        else if(minutes > 1)
        {
            result += minutes + " minutes ";
        }

        result += (seconds <= 1) ? seconds + " seconde " : seconds + " secondes ";
        result += milliseconds;

        return result;
    }

    public boolean timerIsOn()
    {
        return this.timerOn;
    }

    public void addMove()
    {
        this.moves++;
    }

    public void save(int levelId)
    {
        // On crée l'objet JSON score
        JSONObject score = new JSONObject();
        score.put("startedAt", this.startedAt);
        score.put("stoppedAt", this.stoppedAt);
        score.put("moves", this.moves);

        // On récupère les scores sauvegardés
        JSONObject scoresByLevel = this.getScoresByLevel();

        // On tente d'ajouter le nouveau score dans la liste des scores du niveau
        JSONArray levels = (JSONArray) scoresByLevel.get("levels");
        Iterator<JSONObject> iterator = levels.iterator();
        boolean scoreAdded = false;

        // On parcours les niveaux
        while(iterator.hasNext())
        {
            JSONObject level = iterator.next();

            // Si on le trouve, on ajoute le nouveau score
            if(Integer.parseInt(level.get("id").toString()) == levelId)
            {
                JSONArray scores = (JSONArray) level.get("scores");
                scores.add(score);
                scoreAdded = true;
                break;
            }
        }

        // Si on n'a pas trouvé le niveau, on le créer (dans le JSON) et on ajoute le nouveau score
        if(!scoreAdded)
        {
            JSONArray scores = new JSONArray();
            scores.add(score);

            JSONObject level = new JSONObject();
            level.put("id", levelId);
            level.put("scores", scores);

            levels.add(level);
        }

        // On enregistre les modifications dans le fichier JSON
        scoresByLevel.put("levels", levels);

        try
        {
            FileWriter file = new FileWriter(Score.PATH);
            file.write(scoresByLevel.toJSONString());
            file.flush();
            file.close();

        }

        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static JSONObject getScoresByLevel()
    {
        JSONParser parser = new JSONParser();
        JSONObject scoresByLevel = new JSONObject();

        try
        {
            scoresByLevel = (JSONObject) parser.parse(new FileReader(Score.PATH));
        }

        catch(Exception e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return scoresByLevel;
    }

    public static void display(int levelId)
    {
        JSONObject scoresByLevel = Score.getScoresByLevel();

        JSONArray levels = (JSONArray) scoresByLevel.get("levels");
        Iterator<JSONObject> levelsIterator = levels.iterator();

        while(levelsIterator.hasNext())
        {
            JSONObject level = levelsIterator.next();

            if(Integer.parseInt(level.get("id").toString()) == levelId)
            {
                JSONArray scores = (JSONArray) level.get("scores");
                Iterator<JSONObject> scoresIterator = scores.iterator();

                while(scoresIterator.hasNext())
                {
                    JSONObject score = scoresIterator.next();

                    Score currentScore = new Score();
                    currentScore.initialize(score);

                    System.out.println(currentScore.getResult());
                }

                break;
            }
        }
    }

    public void initialize(JSONObject score)
    {
        this.startedAt = Long.parseLong(score.get("startedAt").toString());
        this.stoppedAt = Long.parseLong(score.get("stoppedAt").toString());
        this.moves = Integer.parseInt(score.get("moves").toString());
    }
}
