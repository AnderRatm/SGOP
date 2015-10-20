package com.sgop.fragment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anderson on 11/12/13.
 */
public class Categoria implements Parcelable {
	
	public int id;
    public String descricao;
    public int cod_usr;

    public Categoria( String descricao, int cod_usr) {
        this.descricao = descricao;
        this.cod_usr = cod_usr;
    }


    public Categoria(Parcel in) {
		// TODO Auto-generated constructor stub
    	super();
    	this.id = in.readInt();
		this.descricao = in.readString();
	}

	public Categoria(String desc) {
		// TODO Auto-generated constructor stub
	}


	public Categoria() {
		// TODO Auto-generated constructor stub
		super();
	}


	public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCodigo_usr() {
        return cod_usr;
    }

    public void setCodigo_usr(int cod_usr) {
        this.cod_usr = cod_usr;
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(getId());
		dest.writeString(getDescricao());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public static final Parcelable.Creator<Categoria> CREATOR = new Parcelable.Creator<Categoria>() {
		public Categoria createFromParcel(Parcel in) {
			return new Categoria(in);
		}

		public Categoria[] newArray(int size) {
			return new Categoria[size];
		}
	};
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
