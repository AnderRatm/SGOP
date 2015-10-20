package com.sgop.principal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.projeto.SGOP.R;

/**
 * Created by anderson on 11/12/13.
 */
public class AdapterListView extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ItemListView> itens;

    public AdapterListView(Context context, ArrayList<ItemListView> itens) {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount() {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public ItemListView getItem(int position) {
        return itens.get(position);
    }

    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (view == null) {
            //infla o layout para podermos pegar as views
            view = mInflater.inflate(R.layout.list_boleto, null);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new ItemSuporte();
            itemHolder.txtTitle = ((TextView) view.findViewById(R.id.cadastro));
            itemHolder.txtTitle1 = ((TextView) view.findViewById(R.id.vencimento));
            itemHolder.txtTitle2 = ((TextView) view.findViewById(R.id.codigo));
            //itemHolder.txtTitle3 = ((TextView) view.findViewById(R.id.id_boleto));
            itemHolder.txtTitle4 = ((TextView) view.findViewById(R.id.valor));
            itemHolder.txtTitle5 = ((TextView) view.findViewById(R.id.emissor));

            //define os itens na view;
            view.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (ItemSuporte) view.getTag();
        }

        //pega os dados da lista
        //e define os valores nos itens.
        ItemListView item = itens.get(position);
        //itemHolder.txtTitle.setText(item.getID());
        itemHolder.txtTitle1.setText(item.getCodigo());
        itemHolder.txtTitle2.setText(item.getData_cadastro());
        itemHolder.txtTitle3.setText(item.getData_vencimento());
        itemHolder.txtTitle4.setText(item.getValor());
        itemHolder.txtTitle5.setText(item.getEmissor());

        //retorna a view com as informações
        return view;
    }

    /**
     * Classe de suporte para os itens do layout.
     */
    private class ItemSuporte {

        ImageView imgIcon;
        TextView txtTitle;
         TextView txtTitle1;
        TextView txtTitle2;
        TextView txtTitle3;
        TextView txtTitle4;
        TextView txtTitle5;
    }

}

