package pt.ubi.di.pdm.emotions;

//Bibliotecas Importadas
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;

public class atribuirEmocao extends Activity {

    BDHelper BD_Historico;
    SQLiteDatabase oSQLite;
    ArrayList<String> emocoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atribuir_emocao);

        // Cria o novo objeto, para criar a tabela e ter as funções necessárias;
        BD_Historico = new BDHelper(this);
        // Vai buscar a base de dados, com permissões para abrir e editar (selecionar, alterar e remover);
        oSQLite = BD_Historico.getWritableDatabase();

        Spinner dropdown = findViewById(R.id.drop);
        emocoes = new ArrayList<>();

        Cursor c = oSQLite.rawQuery("SELECT Nome FROM Emocoes;", null);

        while(c.moveToNext()){
            emocoes.add(c.getString(0));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, emocoes);
        dropdown.setAdapter(adapter);
        c.close();
    }
    public void escolherEmocao(View v){
        // Objeto para utilizar na função de inserção na base de dados;
        ContentValues content = new ContentValues();

        Spinner dropdown = findViewById(R.id.drop);
        int posicao = dropdown.getSelectedItemPosition();
        String escolha = emocoes.get(posicao);

        //Regista a data e hora num formato inteiro: (YYYYMMDDhhmm)
        Time dtNow = new Time();
        dtNow.setToNow();
        Long lsNow = Long.parseLong(dtNow.format("%Y%m%d%H%M"));

        Cursor selectId = oSQLite.rawQuery("SELECT Id FROM Emocoes WHERE Nome = '"+ escolha +"'; ", null);
        selectId.moveToFirst();
        int id = selectId.getInt(0);

        // Argumentos que vão ser inseridos na tabela;
        content.put("Horas", lsNow);
        content.put("Id", id);

        oSQLite.insert(BD_Historico.TABLE_NAME_Historico, null, content);
        finish();
    }
    //Volta para Activity anterior
    public void voltar(View v) {
        finish();
    }
}