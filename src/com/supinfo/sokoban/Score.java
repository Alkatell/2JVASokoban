package com.supinfo.sokoban;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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

    /**
     * Enregistre l'heure à laquelle le joueur à commencé le niveau
     */
    public void startTimer()
    {
        this.startedAt = new Date().getTime();
        this.timerOn = true;
    }

    /**
     * * Enregistre l'heure à laquelle le joueur à terminé le niveau
     */
    public void stopTimer()
    {
        this.stoppedAt = new Date().getTime();
        this.timerOn = false;
    }

    /**
     * Renvoie le score réalisé (en faisant une phrase)
     * @return String
     */
    public String getResult()
    {
        long difference = this.stoppedAt - this.startedAt;

        long minutes = difference / (60 * 1000) % 60;
        long seconds = difference / 1000 % 60;
        long milliseconds = difference % 1000;

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

    /**
     * Renvoie vrai si le timer est lancé
     * @return boolean
     */
    public boolean timerIsOn()
    {
        return this.timerOn;
    }

    /**
     * Ajoute un mouvement (pour compter le nombre de mouvements total)
     */
    public void addMove()
    {
        this.moves++;
    }

    /**
     * Sauvegarde le score réalisé dans le fichier JSON
     * @param levelId niveau qui vient d'être terminé
     */
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

    /**
     * Renvoie l'objet JSONObject contenant les scores rangés par niveaux
     * @return JSONObject
     */
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

    /**
     * Affiche les 10 meileurs scores d'un niveau
     * @param levelId niveau dont on veut afficher les scores
     */
    public static void display(int levelId)
    {
        JSONObject scoresByLevel = Score.getScoresByLevel();

        JSONArray levels = (JSONArray) scoresByLevel.get("levels");
        Iterator<JSONObject> levelsIterator = levels.iterator();

        // On recherche le niveau
        while(levelsIterator.hasNext())
        {
            JSONObject level = levelsIterator.next();

            // Si on a des scores pour le niveau demandé
            if(Integer.parseInt(level.get("id").toString()) == levelId)
            {
                JSONArray scores = (JSONArray) level.get("scores");
                Iterator<JSONObject> scoresIterator = scores.iterator();

	            ArrayList<Score> result = new ArrayList<Score>();

                // On parcourt les scores et on les enregistre dans le tableau "result"
                while(scoresIterator.hasNext())
                {
                    JSONObject score = scoresIterator.next();

                    Score currentScore = new Score();
                    currentScore.initialize(score);

					result.add(currentScore);
                }

                // On trie le tableau "result" afin d'afficher les scores dans l'ordre
	            boolean sorted;

				do
				{
					sorted = true;

			        for(int i = 1; i < result.size(); i++)
		            {
						if(result.get(i - 1).getDifference() > result.get(i).getDifference())
						{
							sorted = false;
							Score save = result.get(i - 1);
							result.set(i - 1, result.get(i));
							result.set(i, save);
						}
		            }
				}
				while(!sorted);

                // On affiche les 10 meilleurs scores
                int limit = (result.size() < 10) ? result.size() : 10;

                System.out.println();

	            for(int i = 0; i < limit; i++)
	            {
		            System.out.println("#" + (i + 1) + "\t: " + result.get(i).getResult());
	            }

	            break;
            }
        }
    }

    /**
     * Initialise l'objet Score à partir d'un objet JSONObject
     * @param score object JSONObject dont on veut extraire les données
     */
    public void initialize(JSONObject score)
    {
        this.startedAt = Long.parseLong(score.get("startedAt").toString());
        this.stoppedAt = Long.parseLong(score.get("stoppedAt").toString());
        this.moves = Integer.parseInt(score.get("moves").toString());
    }

    /**
     * Renvoie le nombre de millisecondes écoulées entre l'heure où on a démarré le timer et celle où on l'a stoppé
     * Fonction utilisée pour trier les scores
     * @return int
     */
	public int getDifference()
	{
		return (int) this.stoppedAt - (int) this.startedAt;
	}
}
