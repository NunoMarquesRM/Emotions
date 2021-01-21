package pt.ubi.di.pdm.emotions;

//Bibliotecas Importadas
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class historico extends Activity {
    //Variáveis Globais
    BDHelper BD_Historico;
    SQLiteDatabase oSQLite;
    ArrayList<String> HoraEmocao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        // Cria o novo objeto, para criar a tabela e ter as funções necessárias;
        BD_Historico = new BDHelper(this);
        // Vai buscar a base de dados, com permissões para abrir e editar (selecionar, alterar e remover);
        oSQLite = BD_Historico.getWritableDatabase();
    }


    //Função responsável pela ação do Botão Historico por Dia
    public void porDia(View v){
        //Regista a data e hora num formato inteiro: (YYYYMMDDhhmm)
        Time dtNow = new Time();
        dtNow.setToNow();
        Long lsNow = Long.parseLong(dtNow.format("%Y%m%d"));

        Cursor cursordata = oSQLite.rawQuery("SELECT * FROM Historico WHERE Horas > "+ (lsNow *10000) +" AND Horas < "+ ((lsNow *10000)+2400) +";", null);
        double media = calculoMedia(cursordata);

        cursordata = oSQLite.rawQuery("SELECT * FROM Historico WHERE Horas > "+ (lsNow *10000) +" AND Horas < "+ ((lsNow *10000)+2400) +";", null);
        HoraEmocao = new ArrayList<>();

        while(cursordata.moveToNext()){
            //Retira a String que contem a Data e Hora aquando a Emoçao foi guardada
            Long aux = cursordata.getLong(0);

            //Calculo dos Minutos e aas Horas aquando a Emoção foi guardada
            Long m = (aux)%100;
            Long h = ((aux)% 10000)/100;

            //Retira o valor do id da Base de Dados Historico
            int id = cursordata.getInt(1);

            //Retira o nome da Emoção correspondente ao id acima referido
            Cursor e = oSQLite.rawQuery("SELECT Nome FROM Emocoes WHERE Id = " + id +";",null);
            e.moveToNext();

            //Adiciona ao ArrayList a Data (Dia-Mês),a Hora e a String acima pesquisada
            if(m < 10){
                if(h < 10){
                    HoraEmocao.add("0"+h+":0"+m+"                            "+e.getString(0));
                }
                else{
                    HoraEmocao.add(h+":0"+m+"                            "+e.getString(0));
                }
            }
            else {
                if(h < 10){
                    HoraEmocao.add("0"+h+":"+m+"                            "+e.getString(0));
                }
                else{
                    HoraEmocao.add(h + ":" + m + "                            " + e.getString(0));
                }
            }
            e.close();
        }
        cursordata.close();

        ListView listaData = findViewById(R.id.DataEmocao);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, HoraEmocao);
        listaData.setAdapter(adapter);

        TextView textoNome = findViewById(R.id.textoEstatistica);
        textoNome.setText("Média do Dia Actual: ");

        TextView textoResultado = findViewById(R.id.resultadoEstatistica);
        textoResultado.setText(String.valueOf(media));
    }

    //Função responsável pela ação do Botão Historico por Semana
    public void porSemana(View v){
        //Regista a data e hora num formato inteiro: (YYYYMMDDhhmm)
        Time dtNow = new Time();
        dtNow.setToNow();
        Long lsNow = Long.parseLong(dtNow.format("%Y%m%d"));

        Long auxData;
        Long mothBD,dayBD;
        //Cálculo da Data dos ultimos 7 dias
        if((lsNow %100) < 7){
            auxData = devolveData(lsNow);

        }
        else{
            Long yy,mm,dd;

            yy = lsNow/10000;
            mm = (lsNow %10000)/100;
            dd = lsNow % 100;

            String dataInicio = (yy)+""+(mm)+""+(dd-7)+"";
            auxData = Long.parseLong(dataInicio);
        }
        Cursor cursordata = oSQLite.rawQuery("SELECT * FROM Historico WHERE Horas > "+ (auxData *10000) +" AND Horas < "+ ((lsNow *10000)+2400) +";", null);
        double media = calculoMedia(cursordata);

        cursordata = oSQLite.rawQuery("SELECT * FROM Historico WHERE Horas > "+ (auxData *10000) +" AND Horas < "+ ((lsNow *10000)+2400) +";", null);
        HoraEmocao = new ArrayList<>();

        while(cursordata.moveToNext()){
            //Retira a String que contem a Data e Hora aquando a Emoçao foi guardada
            Long aux = cursordata.getLong(0);

            //Calculo dos Minutos e aas Horas aquando a Emoção foi guardada
            Long m = (aux)%100;
            Long h = ((aux)% 10000)/100;

            //Retira o valor do id da Base de Dados Historico
            int id = cursordata.getInt(1);

            //Retira o nome da Emoção correspondente ao id acima referido
            Cursor e = oSQLite.rawQuery("SELECT Nome FROM Emocoes WHERE Id = " + id +";",null);
            e.moveToNext();

            //Cálculo do mês e dia
            mothBD = ((aux / 10000) % 10000) / 100;
            dayBD = (aux / 10000) % 100;
            //Adiciona ao ArrayList a Data (Dia-Mês),a Hora e a String acima pesquisada
            if(m < 10){
                if(h < 10){
                    HoraEmocao.add(dayBD+"-"+mothBD+"         0"+h+":0"+m+"         "+e.getString(0));
                }
                else{
                    HoraEmocao.add(dayBD+"-"+mothBD+"         "+h+":0"+m+"         "+e.getString(0));
                }
            }
            else {
                if(h < 10){
                    HoraEmocao.add(dayBD+"-"+mothBD+"        0"+h+":"+m+"         "+e.getString(0));
                }
                else{
                    HoraEmocao.add(dayBD+"-"+mothBD+"         "+h+":"+m+"         "+e.getString(0));
                }
            }
            e.close();
        }
        cursordata.close();

        ListView listaData = findViewById(R.id.DataEmocao);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, HoraEmocao);
        listaData.setAdapter(adapter);

        TextView textoNome = findViewById(R.id.textoEstatistica);
        textoNome.setText("Média da última Semana: ");

        TextView textoResultado = findViewById(R.id.resultadoEstatistica);
        textoResultado.setText(String.valueOf(media));
    }
    //Função responsável pela ação do Botão Historico por Mês
    public void porMes(View v){
        //Regista a data e hora num formato inteiro: (YYYYMMDDhhmm)
        Time dtNow = new Time();
        dtNow.setToNow();
        Long lsNow = Long.parseLong(dtNow.format("%Y%m%d"));

        Long auxData;
        Long yy,mm,dd;
        String dataInicio;
        Long mothBD,dayBD;

        //Cálculo do mês presente tendo em conta a transição de ano no mês 1
        yy = lsNow/10000;
        mm = (lsNow %10000)/100;
        dd = lsNow % 100;
        if (mm == 1){
            dataInicio = (yy-1)+""+(12)+""+(dd)+"";
            auxData = Long.parseLong(dataInicio);
        }
        else{
            dataInicio = (yy)+""+(mm-1)+""+(dd)+"";
            auxData = Long.parseLong(dataInicio);
        }
        Cursor cursordata = oSQLite.rawQuery("SELECT * FROM Historico WHERE Horas > "+ (auxData *10000) +" AND Horas < "+ ((lsNow *10000)+2400) +";", null);
        double media = calculoMedia(cursordata);

        cursordata = oSQLite.rawQuery("SELECT * FROM Historico WHERE Horas > "+ (auxData *10000) +" AND Horas < "+ ((lsNow *10000)+2400) +";", null);
        HoraEmocao = new ArrayList<>();

        while(cursordata.moveToNext()){
            //Retira a String que contem a Data e Hora aquando a Emoçao foi guardada
            Long aux = cursordata.getLong(0);

            //Calculo dos Minutos e as Horas aquando a Emoção foi guardada
            Long m = (aux)%100;
            Long h = ((aux)% 10000)/100;

            //Retira o valor do id da Base de Dados Historico
            int id = cursordata.getInt(1);

            //Retira o nome da Emoção correspondente ao id acima referido
            Cursor e = oSQLite.rawQuery("SELECT Nome FROM Emocoes WHERE Id = " + id +";",null);
            e.moveToNext();

            //Cálculo do mês e dia
            mothBD = ((aux / 10000) % 10000) / 100;
            dayBD = (aux / 10000) % 100;

            //Adiciona ao ArrayList a Data (Dia-Mês),a Hora e a String acima pesquisada
            if(m < 10){
                if(h < 10){
                    HoraEmocao.add(dayBD+"-"+mothBD+"         0"+h+":0"+m+"         "+e.getString(0));
                }
                else{
                    HoraEmocao.add(dayBD+"-"+mothBD+"         "+h+":0"+m+"         "+e.getString(0));
                }
            }
            else {
                if(h < 10){
                    HoraEmocao.add(dayBD+"-"+mothBD+"        0"+h+":"+m+"         "+e.getString(0));
                }
                else{
                    HoraEmocao.add(dayBD+"-"+mothBD+"         "+h+":"+m+"         "+e.getString(0));
                }
            }
            e.close();
        }
        cursordata.close();

        ListView listaData = findViewById(R.id.DataEmocao);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, HoraEmocao);
        listaData.setAdapter(adapter);

        TextView textoNome = findViewById(R.id.textoEstatistica);
        textoNome.setText("Média do último Mês: ");

        TextView textoResultado = findViewById(R.id.resultadoEstatistica);
        textoResultado.setText(String.valueOf(media));
    }
    public void voltar(View v){
        finish();
    }

    //Cálculo da Data tendo em conta o presente dia e os ultimos 7 dias
    public Long devolveData(Long val){
        Long yy,mm,dd;
        String dataInicio;

        yy = val/10000;
        mm = (val %10000)/100;
        dd = val % 100;

        if(mm == 1){
            dataInicio = (yy-1)+""+(mm+11)+""+((31-7)+dd)+"";
            return (Long.parseLong(dataInicio));
        }
        else if(mm == 3){
            dataInicio =(yy)+""+(mm-1)+""+((28-7)+dd)+"";
            return (Long.parseLong(dataInicio));
        }
        else if(mm == 2 || mm == 4 || mm == 6 || mm == 8|| mm == 9|| mm == 11){
            dataInicio =(yy)+""+(mm-1)+""+((31-7)+dd)+"";
            return (Long.parseLong(dataInicio));
        }
        else{
            dataInicio =(yy)+""+(mm-1)+""+((30-7)+dd)+"";
            return (Long.parseLong(dataInicio));
        }
    }
    //Calculo da Média dos valores das Emoções
    public double calculoMedia(Cursor a){
        ArrayList<Integer> somad = new ArrayList<>();

        Cursor e;
        int id,v;
        Double media = 0.0;

        while(a.moveToNext()){
            //Retira o valor do id da Base de Dados Historico
            id = a.getInt(1);

            //Retira o valor da Emoção correspondente ao id acima referido
            e = oSQLite.rawQuery("SELECT Valor FROM Emocoes WHERE Id = " + id +";",null);
            e.moveToNext();

            v = e.getInt(0);

            somad.add(v);
            media += v;

            e.close();
        }
        a.close();
        media = (media)/somad.size();

        return media;
    }
}