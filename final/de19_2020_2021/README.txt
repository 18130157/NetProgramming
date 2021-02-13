Thư mục Data nằm cùng cấp folder src.
File format "MãThíSinh.dat"
----------------------------------------
	ID thí sinh
	Name thí sinh
	Dob thí sinh
	Address thí sinh
	Kích thước file ảnh
	Nội dung file ảnh
----------------------------------------

Code được hiện thực theo concept

Khi Client nhập lệnh từ console, Client sẽ kiểm tra
Nếu là hành động SEND_FOTO, Client sẽ check đã có Mã số đăng ký hay chưa, check luôn điều kiện file ảnh hợp lệ

Nếu Mã số đăng ký đã có và điều kiện file ảnh thoả
Client sẽ gửi command dạng SEND_FOTO|file_size cho Server xử lý


Ngược lại nếu không phải hành động SEND_FOTO thì Client gửi command từ console cho Server



Khi vừa REGISTER thành công, Client sẽ có Mã số đăng ký (ví dụ MS002)
Nếu Client tiếp tục REGISTER thành công ngay lúc này, Client sẽ có Mã số đăng ký mới (ví dụ MS003)

Vậy là Thí sinh MS002 sẽ không có foto, nếu bây giờ SEND_FOTO thì Client đang SEND_FOTO cho MS003

File MS002.dat sẽ gồm
----------------------------------------
	ID
	Name
	Dob
	Address
	Kích thước file ảnh = 0
----------------------------------------



