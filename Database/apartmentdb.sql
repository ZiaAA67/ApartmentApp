-- Host: localhost
-- Generation Time: 30-3-2025 at 21:21
-- Author: Mink Nhat, Nghia Le

CREATE DATABASE IF NOT EXISTS `apartmentdb`;
USE `apartmentdb`;

-- Relationship --
CREATE TABLE IF NOT EXISTS `user`(
`id` char(36) PRIMARY KEY DEFAULT (UUID()), -- Sinh mã id tự động không bị trùng
`username` varchar(50) NOT NULL UNIQUE, -- Duy nhất
`password` varchar(255) NOT NULL,
`first_name` varchar(50) NOT NULL,
`last_name` varchar(50) NOT NULL,
`gender` tinyint default 0, -- 0 = male, 1 = female
`birth` date,
`identity_number` varchar(15) NOT NULL UNIQUE,
`email` varchar(50) default null UNIQUE,
`phone` varchar(10) CHECK ((LENGTH(phone)=10 OR LENGTH(phone)=11) AND phone regexp '^0[0-9]+$') NOT NULL UNIQUE, -- Kiểm tra SDT 10 hoặc 11 số và bắt đầu từ số 0
`address` varchar(100),
`role` ENUM('ROLE_ADMIN', 'ROLE_RESIDENT') default 'ROLE_RESIDENT',
`is_first_login` BOOLEAN default 0, -- 0 = never, 1 = has login
`is_active` BOOLEAN default 1, -- 0 = false, 1 = true
`avatar` varchar(333) default null,
`created_date` datetime default CURRENT_TIMESTAMP, -- Lấy thời gian hiện tại khi dòng được thêm
`updated_date` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) engine=InnoDB default charset=utf8mb4;

-- Căn hộ
-- 1 người có thể có nhiều căn hộ, 1 căn hộ chỉ có 1 người đại diện sở hữu
CREATE TABLE IF NOT EXISTS `apartment`(
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`current_owner_id` char(36),
`number` varchar(10) NOT NULL, -- Số phòng
`block` varchar(10) NOT NULL, -- Khu vực
`floor` varchar(10) NOT NULL, -- Tầng
`area` DECIMAL(6,2) NOT NULL, -- Diện tích (m²)
`bathroom` INT default 1, -- Số phòng tắm
`bedroom` INT default 1, -- Số phòng ngủ
-- occupied (đã có người sở hữu), vacant (trống), for_sale (đang rao bán)
`status` ENUM('occupied', 'vacant', 'for_sale') DEFAULT 'vacant', 
`is_active` BOOLEAN default 1, -- 0 = false, 1 = true
FOREIGN KEY (`current_owner_id`) REFERENCES `user`(`id`) ON DELETE SET NULL
) engine=InnoDB default charset=utf8mb4;

-- Khi cùng user và department, sẽ thành update và ghi đè dữ liệu -> mất dữ liệu cũ vì vậy
-- Cần bảng history để lưu lại lịch sử những người đã sở hữu căn hộ
CREATE TABLE IF NOT EXISTS `apartment_history` (
  `id` char(36) PRIMARY KEY DEFAULT (UUID()),
  `apartment_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `action` ENUM('purchased', 'sold') NOT NULL,
  `action_date` datetime default CURRENT_TIMESTAMP,
  `notes` TEXT,
  -- ngăn chặn xoá để toàn vẹn lịch sử, nếu thực sự muốn xoá căn hộ -> xoá lịch sử trước
  -- nhưng thông thường chỉ cần set active = 0
  FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT
) engine=InnoDB;

-- Bảng loại phí dịch vụ (gửi xe, phí quản lý, ...) thuận tiện cho việc thêm các loại dịch vụ khác
CREATE TABLE IF NOT EXISTS `category` (
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`name` varchar(50) UNIQUE NOT NULL,
`is_active` BOOLEAN default 1 -- 0 = false, 1 = true
) engine=InnoDB default charset=utf8mb4;

-- Bảng phương thức thanh toán (MOMO, ZALOPay, ...) thuận tiện cho việc thêm phương thức thanh toán khác
CREATE TABLE IF NOT EXISTS `method`(
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`name` varchar(50) UNIQUE NOT NULL,
`is_active` BOOLEAN default 1 -- 0 = false, 1 = true
) engine=InnoDB default charset=utf8mb4;

-- Bảng lưu thông tin thanh toán
CREATE TABLE IF NOT EXISTS `transaction` (
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`user_id` char(36) NULL, -- Người giao dịch
`apartment_id` char(36) NOT NULL,
`category_id` char(36) NOT NULL, -- Loại dịch vụ thanh toán
`method_id` char(36), -- Phương thúc thanh toán
`amount` DECIMAL(10,2) NOT NULL, -- Số tiền thanh toán
`created_date` datetime default CURRENT_TIMESTAMP, -- Ngày tạo lấy thời gian hiện tại
`due_date` datetime, -- Ngày đáo hạn thanh toán
`updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Ngày cập nhật
`status` ENUM('unpaid', 'pending', 'completed', 'failed') DEFAULT 'unpaid',
`image` varchar(333), -- Upload màn hình ủy nhiệm
-- Để phục vụ thống kê sau này, không được xoá mà chỉ unactive
-- vì user có thể có nhiều căn hộ, nên cần chỉ rõ user thanh toán cho căn hộ nào 
-- hoặc user thanh toán giúp căn hộ của người khác
FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT, -- Xóa user thì vẫn giữ lại các thanh toán của user để phục vụ mục đích thống kê doanh thu của chung cư
FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`id`) ON DELETE RESTRICT,
FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE RESTRICT,-- Khi loại bỏ một loại vẫn giữ lại các thanh toán phục vụ mục đích thống kê doanh thu của chung cư
FOREIGN KEY (`method_id`) REFERENCES `method`(`id`) ON DELETE RESTRICT-- Khi loại bỏ một loại vẫn giữ lại các thanh toán phục vụ mục đích thống kê doanh thu của chung cư
) engine=InnoDB default charset=utf8mb4;

-- Quản lý phương tiện = thẻ giữ xe ( có thể cấp cho người nhà dùng ngắn hạn phụ thuộc access time )
CREATE TABLE IF NOT EXISTS `vehicle_access`(
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`type` ENUM('bicycle','car', 'motorbike', 'other') NOT NULL,
-- `license_plate_image` varchar(50), -- upload hình bằng lái
`number` varchar(20) NOT NULL, -- biển số xe
`brand` VARCHAR(20),
`model` VARCHAR(20),
`color` VARCHAR(20),
`is_active` BOOLEAN default 1,
`is_permanent` BOOLEAN DEFAULT 0, -- thẻ lâu dài ( dành cho cư dân chính thức )
`user_id` char(36), -- Người đăng ký thẻ xe
`created_date` datetime default CURRENT_TIMESTAMP,
`access_time` TIMESTAMP, -- thời gian có hiệu lực ( dùng cho người nhà cư dân )
FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
CHECK (is_permanent = 1 OR user_id IS NOT NULL) -- Nếu is_permanent = 0 thì user_id phải khác NULL
-- Chỉ khi xóa hết phương tiện do user A đăng ký mới có thể xóa user A khỏi User
-- user A có thể đăng ký nhiều phương tiện, người sở hữu sẽ xem ở upload picture license_plate
) engine=InnoDB;

-- Quản lý thẻ ra vào cổng ( chỉ cấp cho cư dân chính thức )
CREATE TABLE IF NOT EXISTS `access_card`(
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`user_id` char(36),
`is_active` BOOLEAN default 1,
`created_date` datetime default CURRENT_TIMESTAMP,
FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
-- Nếu User bị rời chung cư thì xóa hết access_card xóa thẻ (tránh user rời rồi vẫn có thể ra vào chung cư) 
-- --> set active = 0, không cần xoá
) engine=InnoDB;

-- Tạo bảng electronic_locker (Tủ đồ điện tử), mỗi dân cư 1 tủ đồ điện tử
-- --> mỗi căn hộ có 1 tủ đồ, gửi noti đến người sở hữu căn hộ 
-- hiển thị số lượng đơn bằng cách đếm khi xử lý dữ liệu, kh lưu cứng trong db
CREATE TABLE IF NOT EXISTS `delivery` (
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`apartment_id` char(36) NOT NULL,
-- `status` BOOLEAN default 0, -- 0 = rỗng, 1 = có hàng
`recipient_name` VARCHAR(100) NOT NULL, -- tên người nhận
`package_description` TEXT,
`arrived_at` datetime default CURRENT_TIMESTAMP, -- thời điểm bảo vệ nhận hàng
`delivered_at` TIMESTAMP, -- thời điểm người nhận lấy hàng
-- ngoài nhận hàng, có thể trả hoặc gửi ngược lại bằng status returned
`status` ENUM('waiting', 'delivered', 'returned') NOT NULL DEFAULT 'waiting',
`notes` TEXT,
-- FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`id`) ON DELETE CASCADE
-- Xóa user thì xóa tủ luôn
) engine=InnoDB;

-- Phản ánh của người dân
CREATE TABLE IF NOT EXISTS `complaint` (
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`user_id` char(36) NOT NULL,
`title` VARCHAR(100) NOT NULL,
`content` TEXT NOT NULL,
`date_submitted` datetime default CURRENT_TIMESTAMP, -- Ngày gửi
`status` ENUM('pending', 'in_progress', 'resolved', 'rejected') NOT NULL DEFAULT 'pending',
FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
-- Xóa user thì xóa luôn các phản ánh (ko có giá trị lưu trữ)
) engine=InnoDB;

-- Tạo bảng survey (Khảo sát)
CREATE TABLE IF NOT EXISTS `survey` (
  `id` char(36) PRIMARY KEY DEFAULT (UUID()),
  `user_id` char(36) NOT NULL, -- người tạo ks
  `title` VARCHAR(100) NOT NULL,
  `content` TEXT,
  `created_date` datetime default CURRENT_TIMESTAMP,
  `updated_date` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` BOOLEAN default 1, -- đóng / mở khảo sát thay vì status
  -- `status` ENUM('active', 'completed') DEFAULT 'active'
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) engine=InnoDB;

-- Tạo bảng survey_response (Phản hồi khảo sát)
CREATE TABLE IF NOT EXISTS `survey_response` (
`id` char(36) PRIMARY KEY DEFAULT (UUID()),
`survey_id` char(36) NOT NULL,
`user_id` char(36) NOT NULL,
`response` TEXT NOT NULL,
`submitted_date` datetime default CURRENT_TIMESTAMP,
FOREIGN KEY (`survey_id`) REFERENCES `survey`(`id`) ON DELETE CASCADE,
FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) engine=InnoDB;

-- Bảng user
INSERT INTO `user` (`username`, `password`, `first_name`, `last_name`, `gender`, `birth`, `identity_number`, `email`, `phone`, `address`, `role`, `is_first_login`, `is_active`) VALUES
('admin1', '123', 'Admin', 'One', 0, '1990-01-15', '1234567890123', 'admin1@example.com', '0901234567', '123 Đường ABC, Quận XYZ, TP.HCM', 'ROLE_ADMIN', 0, 1),
('resident1', '123', 'Nguyen', 'Van A', 0, '1995-05-20', '0987654321098', 'vana@example.com', '0911223344', '456 Đường DEF, Quận UVW, TP.HCM', 'ROLE_RESIDENT', 1, 1),
('resident2', '123', 'Tran', 'Thi B', 1, '1992-11-10', '1122334455667', 'thib@example.com', '0922334455', '789 Đường GHI, Quận STU, TP.HCM', 'ROLE_RESIDENT', 0, 1),
('resident3', '123', 'Le', 'Van C', 0, '1988-07-25', '3344556677889', 'vanc@example.com', '0933445566', '101 Đường JKL, Quận OPQ, TP.HCM', 'ROLE_RESIDENT', 0, 0);

-- Bảng apartment
INSERT INTO `apartment` (`current_owner_id`, `number`, `block`, `floor`, `area`, `bathroom`, `bedroom`, `status`, `is_active`) VALUES
((SELECT id FROM `user` WHERE username = 'resident1'), '101A', 'A', '1', 55.50, 1, 2, 'occupied', 1),
(NULL, '102A', 'A', '1', 60.00, 2, 2, 'vacant', 1),
((SELECT id FROM `user` WHERE username = 'resident2'), '205B', 'B', '2', 70.25, 2, 3, 'occupied', 1),
(NULL, '310C', 'C', '3', 45.75, 1, 1, 'for_sale', 1);

-- Bảng apartment_history
INSERT INTO `apartment_history` (`apartment_id`, `user_id`, `action`, `action_date`) VALUES
((SELECT id FROM `apartment` WHERE number = '101A'), (SELECT id FROM `user` WHERE username = 'resident1'), 'purchased', '2025-03-15 10:00:00'),
((SELECT id FROM `apartment` WHERE number = '205B'), (SELECT id FROM `user` WHERE username = 'resident2'), 'purchased', '2025-03-20 14:30:00');

-- Bảng category
INSERT INTO `category` (`name`) VALUES
('Phí quản lý'),
('Phí gửi xe máy'),
('Phí gửi ô tô'),
('Phí vệ sinh');

-- Bảng method
INSERT INTO `method` (`name`) VALUES
('Tiền mặt'),
('Chuyển khoản ngân hàng'),
('MOMO'),
('ZaloPay');

-- Bảng transaction
INSERT INTO `transaction` (`user_id`, `apartment_id`, `category_id`, `method_id`, `amount`, `status`) VALUES
((SELECT id FROM `user` WHERE username = 'resident1'), (SELECT id FROM `apartment` WHERE number = '101A'), (SELECT id FROM `category` WHERE name = 'Phí quản lý'), (SELECT id FROM `method` WHERE name = 'Chuyển khoản ngân hàng'), 150000.00, 'completed'),
((SELECT id FROM `user` WHERE username = 'resident2'), (SELECT id FROM `apartment` WHERE number = '205B'), (SELECT id FROM `category` WHERE name = 'Phí gửi xe máy'), (SELECT id FROM `method` WHERE name = 'MOMO'), 50000.00, 'completed'),
((SELECT id FROM `user` WHERE username = 'resident1'), (SELECT id FROM `apartment` WHERE number = '101A'), (SELECT id FROM `category` WHERE name = 'Phí gửi ô tô'), (SELECT id FROM `method` WHERE name = 'Tiền mặt'), 200000.00, 'pending');

-- Bảng vehicle_access
INSERT INTO `vehicle_access` (`type`, `number`, `brand`, `model`, `color`, `is_permanent`, `user_id`) VALUES
('motorbike', 'ABC-12345', 'Honda', 'Wave Alpha', 'Red', 1, (SELECT id FROM `user` WHERE username = 'resident1')),
('car', 'XYZ-9876', 'Toyota', 'Vios', 'Black', 1, (SELECT id FROM `user` WHERE username = 'resident2')),
('bicycle', 'BIKE-001', 'Giant', '', 'Blue', 0, (SELECT id FROM `user` WHERE username = 'resident1'));

-- Bảng access_card
INSERT INTO `access_card` (`user_id`) VALUES
((SELECT id FROM `user` WHERE username = 'resident1')),
((SELECT id FROM `user` WHERE username = 'resident2'));

-- Bảng delivery
INSERT INTO `delivery` (`apartment_id`, `recipient_name`, `package_description`, `status`) VALUES
((SELECT id FROM `apartment` WHERE number = '101A'), 'Nguyen Van A', 'Quần áo', 'delivered'),
((SELECT id FROM `apartment` WHERE number = '205B'), 'Tran Thi B', 'Đồ gia dụng nhỏ', 'waiting');

-- Bảng complaint
INSERT INTO `complaint` (`user_id`, `title`, `content`, `status`) VALUES
((SELECT id FROM `user` WHERE username = 'resident1'), 'Tiếng ồn lớn', 'Hàng xóm làm ồn quá khuya.', 'in_progress'),
((SELECT id FROM `user` WHERE username = 'resident2'), 'Vệ sinh chưa tốt', 'Khu vực hành lang chưa được vệ sinh sạch sẽ.', 'pending');

-- Bảng survey
INSERT INTO `survey` (`user_id`, `title`, `content`, `is_active`) VALUES
((SELECT id FROM `user` WHERE username = 'admin1'), 'Khảo sát về chất lượng dịch vụ', 'Vui lòng cho biết ý kiến của bạn về chất lượng dịch vụ của chung cư.', 1);

-- Bảng survey_response
INSERT INTO `survey_response` (`survey_id`, `user_id`, `response`) VALUES
((SELECT id FROM `survey` WHERE title = 'Khảo sát về chất lượng dịch vụ'), (SELECT id FROM `user` WHERE username = 'resident1'), 'Tôi khá hài lòng với dịch vụ hiện tại.'),
((SELECT id FROM `survey` WHERE title = 'Khảo sát về chất lượng dịch vụ'), (SELECT id FROM `user` WHERE username = 'resident2'), 'Cần cải thiện vấn đề vệ sinh.');