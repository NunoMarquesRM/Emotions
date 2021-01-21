package pt.ubi.di.pdm.emotions;

//Bibliotecas Importadas
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class BDHelper extends SQLiteOpenHelper
{
    // Versão da base de dados
    private static final int DB_Version = 1;

    // Nome da base de dados
    private static final String DB_NAME = "Emocao_DB";

    //Criar a tabela com nome e colunas
    public static final String TableEmocoes = "CREATE TABLE IF NOT EXISTS Emocoes(Id INTEGER PRIMARY KEY, Nome varchar(255), Valor int);";
    public static final String TableHistorico = "CREATE TABLE IF NOT EXISTS Historico(Horas BIGINT, Id int);";

    protected static final String TABLE_NAME_Emocoes = "Emocoes";
    protected static final String TABLE_NAME_Historico = "Historico";

    // Construtor para a classe;
    public BDHelper(Context context)
    {
        super(context, DB_NAME, null, DB_Version);
    }

    // Função que é chamada automaticamente quando o objeto é criado;
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Executa a query correspondente à criação da tabela;
        db.execSQL(TableEmocoes);
        db.execSQL("INSERT INTO Emocoes(Nome, Valor) VALUES('Curioso',65),('Preocupado',38),('Sozinho',8),('Motivado',83)," +
                "('Indiferente',50),('Triste',20),('Engraçado',75),('Medo',24),('Sortudo',68),('Confuso',45),('Energético',95)," +
                "('Tranquilo',60),('Feliz',80),('Aborrecido',36),('Ansioso',28),('Emocional',40),('Agradecido',64)," +
                "('Apaixonado',88),('Deprimido',10),('Vazio',15),('Fantástico',85),('Zangado',13),('Entusiasmado',90);");

        db.execSQL(TableHistorico);
    }

    // Função que é chamada quando há um "upgrade" manual na base de dados;
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Executa a query correspondente à eliminação da tabela;
        db.execSQL("DROP TABLE IF EXISTS TableEmocoes");
        db.execSQL("DROP TABLE IF EXISTS TableHistorico");

        // Executa a query correspondente à criação da tabela;
        db.execSQL(TableEmocoes);
        db.execSQL(TableHistorico);
    }
}
