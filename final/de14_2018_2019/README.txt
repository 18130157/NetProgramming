Thư mục Data nằm cùng cấp folder src.
File format info.data
----------------------------------------
	Số thí sinh

	ID thí sinh 1
	Name thí sinh 1
	Dob thí sinh 1
	Address thí sinh 1

	ID thí sinh 2
	Name thí sinh 2
	Dob thí sinh 2
	Address thí sinh 2

	...

	ID thí sinh n
	Name thí sinh n
	Dob thí sinh n
	Address thí sinh n
----------------------------------------

Chương trình hoạt động theo 2 Phase

Phase 1: Chỉ chấp nhận các command REGISTER, VIEW_INFO, QUIT
Nếu REGISTER thành công thì vào Phase 2.
VIEW_INFO được hiện thực giống kiểu lookup
Không bắt buộc mình phải là người register được ID MSXXX. Mình vẫn có thể VIEW_INFO của MSXXX.

Phase 2: Chỉ chấp nhận các command SEND_FOTO, QUIT
Chỉ cho phép upload foto trong phase này
Nếu upload thành công sẽ trở về Phase 1
Nếu QUIT thì coi như thí sinh này không có foto.




				CONFUSED
SessionID là số int, ID của thí sinh cũng được tạo từ SessionID.
Ví dụ: 1 -> MS001, 1234 -> MS1234

Map<Integer, String>	Key là SessionID, String là ID của thí sinh

Khi Client REGISTER thành công. Response sẽ là ID thí sinh.
Vậy còn SessionID ?
	(1) Client sẽ tự tái tạo ra SessionID từ ID thí sinh
	(2) Hay sẽ có method getSessionID() của Remote Interface để Client gọi

Dùng (1) sẽ nhanh hơn (2) ? Vì (2) phải duyệt qua các Entry ? Tạo thêm 1 Map ? 
Nếu bên Server dùng cấu trúc dữ liệu dạng giống BidiMap sẽ okay hơn ? get Key from Value ?


Nhiều thread write xuống 1 file
	(3) Method save() sẽ được synchronized. Khi cần lưu thí sinh thì tạo ra 1 thread gọi save().
		-> Mở file nhiều lần -> chi phí cao ?
	(4) Dùng BlockingQueue
		-> Mở file 1 lần ?


Hay là do ban đầu, việc xác định các method cho Remote Interface đã không tốt ?



