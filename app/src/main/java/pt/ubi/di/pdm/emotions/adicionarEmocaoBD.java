package pt.ubi.di.pdm.emotions;

//Bibliotecas Importadas
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import static java.lang.Integer.parseInt;

public class adicionarEmocaoBD extends Activity {

    BDHelper BD_Emocao;
    SQLiteDatabase oSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_emocao);

        // Cria o novo objeto, para criar a tabela e ter as funções necessárias;
        BD_Emocao = new BDHelper(this);
        // Vai buscar a base de dados, com permissões para abrir e editar (selecionar, alterar e remover);
        oSQLite = BD_Emocao.getWritableDatabase();
    }

    public void guardarEmocao(View v) {
        EditText nome = (EditText) findViewById(R.id.nome);
        EditText valor = (EditText) findViewById(R.id.valor);

        //Notificação "aviso" para alertar o utilizador
        Context context = getApplicationContext();
        Toast aviso = Toast.makeText(context, "Um dos valores introduzidos não está correto!",Toast.LENGTH_LONG);

        //Flag criada para ajudar a verificar se o nome já existe na Base de Dados e se o valor é entre 0 e 100!
        Boolean flag = false;

        // Cria-se um cursor, para guardar os dados em relação à query de SELECT que foi utilizada;
        Cursor c = oSQLite.rawQuery("SELECT Nome FROM Emocoes;", null);

        //Verifica se o nome da Emoção já existe na Base de Dados
        while(c.moveToNext()){
            if(c.getString(0).toLowerCase().equals(nome.getText().toString().toLowerCase())){
                flag = true;
                c.close();
                break;
            }
        }
        //Verifica se um dos inputs é vazio
        if(valor.getText().toString().equals("") || nome.getText().toString().equals("")){
            flag = true;
        }
        else{
            if((parseInt(valor.getText().toString()) < 0) || (parseInt(valor.getText().toString()) > 100)){
                flag = true;
            }
        }
        //Caso alguma das verificações acima se confirme o aviso é mostrado ao utilizador
        if(flag){
            aviso.show();
        }
        //Caso não se verifique vai ser adicionado os dois valores (Nome e Valor) à BD
        else {
            // Objeto para utilizar na função de inserção na base de dados;
            ContentValues content = new ContentValues();

            // Argumentos que vão ser inseridos na tabela;
            content.put("Nome", nome.getText().toString());
            content.put("Valor", parseInt(valor.getText().toString()));

            //Adiciona o novo conteúdo à BD
            oSQLite.insert(BD_Emocao.TABLE_NAME_Emocoes, null, content);

            c.close();
            finish();
        }
    }
    //Volta para Activity anterior
    public void voltar(View v) {
        finish();
    }
}
