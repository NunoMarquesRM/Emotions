package pt.ubi.di.pdm.emotions;

//Bibliotecas Importadas
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //Função responsável pela ação do Botão Emoção Atual
    public void novaEmocao(View v){
        Intent abrirNovaEmocao = new Intent(this, atribuirEmocao.class);
        startActivity(abrirNovaEmocao);
    }
    //Função responsável pela ação do Botão Adicionar Emoção (há Base de Dados)
    public void addEmocao(View v){
        Intent abrirAddEmocao = new Intent(this, adicionarEmocaoBD.class);
        startActivity(abrirAddEmocao);
    }
    //Função responsável pela ação do Botão Histórico
    public void historico(View v){
        Intent abrirHistorico = new Intent(this, historico.class);
        startActivity(abrirHistorico);
    }
}