package ch.hevs.aislab.intro.database.async;

import android.content.Context;
import android.os.AsyncTask;

import ch.hevs.aislab.intro.database.AppDatabase;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;

public class CreateClient extends AsyncTask<ClientEntity, Void, Void> {

    private AppDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;        // On garde l'exception en variable !!!

    public CreateClient(Context context, OnAsyncEventListener callback) {
        database = AppDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(ClientEntity... params)  // On peut reçevoir plusieurs paramètres, une liste de paramètres
    {
        try {
            for (ClientEntity client : params)
                database.clientDao().insert(client);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) 
    {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();       // Dans les classes plus haut. Afficher un message de succès genre "Client bien ajouté"
            } else {
                callback.onFailure(exception); // Dans les classes plus haut. Afficher un message de error genre "Pas de réseau"
            }
        }
    }
}
