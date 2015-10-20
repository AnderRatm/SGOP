package com.sgop.fragment;

import java.util.Date;

import com.sgop.fragment.Categoria;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anderson on 24/11/13.
 */
public class Boleto implements Parcelable {

	int id;
	String cod_boleto;
	String instituicao;
	String dt_venc;
	double valor_calc;
	String dt_cad;
	int codigo_usr;
	int Codigo_categoria;
	int Codigo_status;
	private Categoria categoria;
	private Status status;

	public Boleto() {
		super();
	}

	private Boleto(Parcel in) {
		super();
		this.id = in.readInt();
		this.cod_boleto = in.readString();
		this.instituicao = in.readString();
		this.dt_venc = in.readString();
		this.valor_calc = in.readDouble();
		this.dt_cad = in.readString();
		this.codigo_usr = in.readInt();
		this.Codigo_categoria = in.readInt();
		this.Codigo_status = in.readInt();

		// this.dateOfBirth = new Date(in.readLong());
		this.categoria = in.readParcelable(Categoria.class.getClassLoader());
	}

	public static class Status extends Boleto {
		int cod_status;
		String desc;

		public int getCod_status() {
			return cod_status;
		}

		public void setCod_status(int cod_status) {
			this.cod_status = cod_status;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Status(int cod_status, String desc) {
			this.cod_status = cod_status;
			this.desc = desc;

		}

		public Status() {
			// TODO Auto-generated constructor stub
			super();
		}

		@Override
		public String getInstituicao() {
			// TODO Auto-generated method stub
			return super.getInstituicao();
		}

		@Override
		public void setInstituicao(String instituicao) {
			// TODO Auto-generated method stub
			super.setInstituicao(instituicao);
		}

		@Override
		public String getDt_venc() {
			// TODO Auto-generated method stub
			return super.getDt_venc();
		}

		@Override
		public void setDt_venc(String dt_venc) {
			// TODO Auto-generated method stub
			super.setDt_venc(dt_venc);
		}

		@Override
		public double getValor_calc() {
			// TODO Auto-generated method stub
			return super.getValor_calc();
		}

		@Override
		public void setValor_calc(double valor_calc) {
			// TODO Auto-generated method stub
			super.setValor_calc(valor_calc);
		}

		@Override
		public String getDt_cad() {
			// TODO Auto-generated method stub
			return super.getDt_cad();
		}

		@Override
		public void setDt_cad(String dt_cad) {
			// TODO Auto-generated method stub
			super.setDt_cad(dt_cad);
		}

		@Override
		public String getCod_boleto() {
			// TODO Auto-generated method stub
			return super.getCod_boleto();
		}

		@Override
		public void setCod_boleto(String cod_boleto) {
			// TODO Auto-generated method stub
			super.setCod_boleto(cod_boleto);
		}

		@Override
		public int getCodigo_usr() {
			// TODO Auto-generated method stub
			return super.getCodigo_usr();
		}
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public String getDt_venc() {
		return dt_venc;
	}

	public void setDt_venc(String dt_venc) {
		this.dt_venc = dt_venc;
	}

	public double getValor_calc() {
		return valor_calc;
	}

	public void setValor_calc(double valor_calc) {
		this.valor_calc = valor_calc;
	}

	public String getDt_cad() {
		return dt_cad;
	}

	public void setDt_cad(String dt_cad) {
		this.dt_cad = dt_cad;
	}

	public String getCod_boleto() {
		return cod_boleto;
	}

	public void setCod_boleto(String cod_boleto) {
		this.cod_boleto = cod_boleto;
	}

	public Boleto(String cod_boleto, String instituicao, String dt_venc,
			double valor_calc, String dt_cad, int cod_usuario,
			int cod_categoria, int cod_status) {
		this.cod_boleto = cod_boleto;
		this.instituicao = instituicao;
		this.dt_venc = dt_venc;
		this.dt_cad = dt_cad;
		this.valor_calc = valor_calc;
		this.codigo_usr = cod_usuario;
		this.Codigo_categoria = cod_categoria;
		this.Codigo_status = cod_status;
	}

	public int getCodigo_usr() {

		return codigo_usr;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public void setStatus(Status st) {
		this.status = st;
	}

	public int getCodigo_categoria() {
		return Codigo_categoria;
	}

	public void setCodigo_categoria(int codigo_categoria) {
		Codigo_categoria = codigo_categoria;
	}

	public int getCodigo_status() {
		return Codigo_status;
	}

	public void setCodigo_status(int codigo_status) {
		Codigo_status = codigo_status;
	}

	public void setCodigo_usr(int codigo_usr) {
		this.codigo_usr = codigo_usr;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// TODO Auto-generated method stub
		parcel.writeString(getCod_boleto());
		parcel.writeString(getInstituicao());
		parcel.writeString(getDt_venc());
		parcel.writeDouble(getValor_calc());
		parcel.writeString(getDt_cad());
		parcel.writeInt(getCodigo_usr());
		parcel.writeInt(getCodigo_categoria());
		parcel.writeInt(getCodigo_status());
		parcel.writeParcelable(getCategoria(), flags);
		// parcel.writeLong(getDateOfBirth().getTime());
	}

	public static final Parcelable.Creator<Boleto> CREATOR = new Parcelable.Creator<Boleto>() {
		public Boleto createFromParcel(Parcel in) {
			return new Boleto(in);
		}

		public Boleto[] newArray(int size) {
			return new Boleto[size];
		}
	};

	@Override
	public String toString() {
		return "Boleto [cod_boleto=" + cod_boleto + ", instituicao="
				+ instituicao + ", dt_venc=" + dt_venc + ", valor_calc="
				+ valor_calc + ", dt_cad=" + dt_cad + ", codigo_usr="
				+ codigo_usr + ", Codigo_categoria=" + Codigo_categoria
				+ ", Codigo_status=" + Codigo_status + ", categoria="
				+ categoria + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
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
		Boleto other = (Boleto) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setDt_venc(Date parse) {
		// TODO Auto-generated method stub
		
	}

	public Status getStatus() {
		return status;
	}
}