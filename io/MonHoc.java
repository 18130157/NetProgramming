import java.io.Serializable;

public class MonHoc implements Serializable {

	private static final long serialVersionUID = 1L;
	private String maMH;
	private String tenMH;
	private double diem;

	public MonHoc() {
	}

	public MonHoc(String maMH, String tenMH, double diem) {
		this.maMH = maMH;
		this.tenMH = tenMH;
		this.diem = diem;
	}

	@Override
	public String toString() {
		return tenMH + " " + diem + "đ";
	}

	public String getMaMH() {
		return maMH;
	}

	public void setMaMH(String maMH) {
		this.maMH = maMH;
	}

	public String getTenMH() {
		return tenMH;
	}

	public void setTenMH(String tenMH) {
		this.tenMH = tenMH;
	}

	public double getDiem() {
		return diem;
	}

	public void setDiem(double diem) {
		this.diem = diem;
	}

}
