-- Gán sẵn ID cho các khóa ngoại (lấy dòng đầu tiên của mỗi bảng)
SET @user_id = (SELECT id FROM `apartmentdb`.`user` LIMIT 1);
SET @apartment_id = (SELECT id FROM `apartmentdb`.`apartment` LIMIT 1);
SET @category_id = (SELECT id FROM `apartmentdb`.`category` LIMIT 1);
SET @method_id = (SELECT id FROM `apartmentdb`.`method` LIMIT 1);

-- Chèn 12 dòng
INSERT INTO `apartmentdb`.`transaction` (
    `id`,
    `user_id`,
    `apartment_id`,
    `category_id`,
    `method_id`,
    `amount`,
    `created_date`,
    `status`,
    `image`
)
VALUES
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 100000, CURRENT_TIMESTAMP, 'pending', 'img1.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 110000, CURRENT_TIMESTAMP, 'pending', 'img2.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 120000, CURRENT_TIMESTAMP, 'pending', 'img3.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 130000, CURRENT_TIMESTAMP, 'pending', 'img4.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 140000, CURRENT_TIMESTAMP, 'pending', 'img5.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 150000, CURRENT_TIMESTAMP, 'pending', 'img6.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 160000, CURRENT_TIMESTAMP, 'pending', 'img7.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 170000, CURRENT_TIMESTAMP, 'pending', 'img8.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 180000, CURRENT_TIMESTAMP, 'pending', 'img9.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 190000, CURRENT_TIMESTAMP, 'pending', 'img10.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 200000, CURRENT_TIMESTAMP, 'pending', 'img11.jpg'),
(UUID(), @user_id, @apartment_id, @category_id, @method_id, 210000, CURRENT_TIMESTAMP, 'pending', 'img12.jpg');

-- Xem kết quả
SELECT * FROM apartmentdb.transaction;
