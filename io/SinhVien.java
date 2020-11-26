import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SinhVien implements Serializable {
	private static final long serialVersionUID = 1L;
	private String maSV;
	private String hoTen;
	private int tuoi;
	private double dTB;
	private List<MonHoc> dsMonHoc;

	public SinhVien() {
	}

	public SinhVien(String maSV, String hoTen, int tuoi, double dTB) {
		this.maSV = maSV;
		this.hoTen = hoTen;
		this.tuoi = tuoi;
		this.dTB = dTB;
	}

	public SinhVien(String maSV, String hoTen) {
		this.maSV = maSV;
		this.hoTen = hoTen;
		this.tuoi = 20;
		this.dsMonHoc = new ArrayList<MonHoc>();
	}

	public void save(DataOutput stream) throws IOException {
		stream.writeUTF(maSV);
		stream.writeUTF(hoTen);
		stream.writeInt(tuoi);
		stream.writeDouble(dTB);
	}

	public void load(DataInput stream) throws IOException {
		maSV = stream.readUTF();
		hoTen = stream.readUTF();
		tuoi = stream.readInt();
		dTB = stream.readDouble();
	}

	public String line(String delimited) {
		StringBuilder stb = new StringBuilder(maSV + delimited + hoTen);
		for (MonHoc m : dsMonHoc)
			stb.append(delimited + m.getTenMH() + delimited + m.getDiem());
		return stb.toString();
	}

	@Override
	public String toString() {
		return String.format("Tên sinh viên: %s ———— Mã SV: %s\nTuổi: %d ———— Điểm TB: %.2f", hoTen, maSV, tuoi, dTB);
	}

	public String toString2() {
		StringBuilder stb = new StringBuilder();
		for (MonHoc m : dsMonHoc)
			stb.append(m + ", ");
		return (dsMonHoc.size() > 0) ? String.format("%s — %s: %s", maSV, hoTen, stb.substring(0, stb.length() - 2))
				: String.format("%s — %s", maSV, hoTen);
	}

	public void addMonHoc(MonHoc m) {
		dsMonHoc = (dsMonHoc == null) ? new ArrayList<MonHoc>() : dsMonHoc;
		dsMonHoc.add(m);
	}

	public void addMonHoc(MonHoc... mh) {
		dsMonHoc = (dsMonHoc == null) ? new ArrayList<MonHoc>() : dsMonHoc;
		for (MonHoc m : mh)
			dsMonHoc.add(m);
	}

	public String getMaSV() {
		return maSV;
	}

	public void setMaSV(String maSV) {
		this.maSV = maSV;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public int getTuoi() {
		return tuoi;
	}

	public void setTuoi(int tuoi) {
		this.tuoi = tuoi;
	}

	public double getdTB() {
		return dTB;
	}

	public void setdTB(double dTB) {
		this.dTB = dTB;
	}

	public List<MonHoc> getDsMonHoc() {
		return dsMonHoc;
	}

	public void setDsMonHoc(List<MonHoc> dsMonHoc) {
		this.dsMonHoc = dsMonHoc;
	}

}
