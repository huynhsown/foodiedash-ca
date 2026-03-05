-- ============================================
-- BULK DATA FOR FOOD DELIVERY PLATFORM
-- Ho Chi Minh City, Vietnam
-- ============================================

-- Restaurant Categories (20 categories)
INSERT INTO restaurant_categories (id, name, icon_url, description, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
(1, 'Cơm', 'https://cdn-icons-png.flaticon.com/512/1046/1046784.png', 'Cơm tấm, cơm gà, cơm niêu và các món cơm truyền thống', NOW(), NOW(), 'system', 'system', NULL, 0),
(2, 'Phở', 'https://cdn-icons-png.flaticon.com/512/1046/1046785.png', 'Phở bò, phở gà và các loại phở đặc trưng Việt Nam', NOW(), NOW(), 'system', 'system', NULL, 0),
(3, 'Bún', 'https://cdn-icons-png.flaticon.com/512/1046/1046786.png', 'Bún bò Huế, bún chả, bún riêu và các món bún', NOW(), NOW(), 'system', 'system', NULL, 0),
(4, 'Bánh mì', 'https://cdn-icons-png.flaticon.com/512/1046/1046787.png', 'Bánh mì thịt nướng, bánh mì chả cá và các loại bánh mì', NOW(), NOW(), 'system', 'system', NULL, 0),
(5, 'Gà rán', 'https://cdn-icons-png.flaticon.com/512/1046/1046788.png', 'Gà rán giòn, gà nướng và các món gà', NOW(), NOW(), 'system', 'system', NULL, 0),
(6, 'Pizza', 'https://cdn-icons-png.flaticon.com/512/1046/1046789.png', 'Pizza Ý, pizza Mỹ và các loại pizza', NOW(), NOW(), 'system', 'system', NULL, 0),
(7, 'Burger', 'https://cdn-icons-png.flaticon.com/512/1046/1046790.png', 'Burger bò, burger gà và các loại burger', NOW(), NOW(), 'system', 'system', NULL, 0),
(8, 'Mì Ý', 'https://cdn-icons-png.flaticon.com/512/1046/1046791.png', 'Spaghetti, lasagna và các món mì Ý', NOW(), NOW(), 'system', 'system', NULL, 0),
(9, 'Sushi', 'https://cdn-icons-png.flaticon.com/512/1046/1046792.png', 'Sushi, sashimi và các món Nhật', NOW(), NOW(), 'system', 'system', NULL, 0),
(10, 'Lẩu', 'https://cdn-icons-png.flaticon.com/512/1046/1046793.png', 'Lẩu Thái, lẩu nướng và các loại lẩu', NOW(), NOW(), 'system', 'system', NULL, 0),
(11, 'Nướng', 'https://cdn-icons-png.flaticon.com/512/1046/1046794.png', 'Thịt nướng, hải sản nướng', NOW(), NOW(), 'system', 'system', NULL, 0),
(12, 'Chè', 'https://cdn-icons-png.flaticon.com/512/1046/1046795.png', 'Chè đậu xanh, chè khúc bạch và các loại chè', NOW(), NOW(), 'system', 'system', NULL, 0),
(13, 'Cà phê', 'https://cdn-icons-png.flaticon.com/512/1046/1046796.png', 'Cà phê đen, cà phê sữa và các loại cà phê', NOW(), NOW(), 'system', 'system', NULL, 0),
(14, 'Trà sữa', 'https://cdn-icons-png.flaticon.com/512/1046/1046797.png', 'Trà sữa trân châu, trà sữa thái và các loại trà sữa', NOW(), NOW(), 'system', 'system', NULL, 0),
(15, 'Bánh', 'https://cdn-icons-png.flaticon.com/512/1046/1046798.png', 'Bánh ngọt, bánh kem và các loại bánh', NOW(), NOW(), 'system', 'system', NULL, 0),
(16, 'Hải sản', 'https://cdn-icons-png.flaticon.com/512/1046/1046799.png', 'Tôm, cua, cá và các món hải sản', NOW(), NOW(), 'system', 'system', NULL, 0),
(17, 'Chay', 'https://cdn-icons-png.flaticon.com/512/1046/1046800.png', 'Các món chay và đồ chay', NOW(), NOW(), 'system', 'system', NULL, 0),
(18, 'Đồ uống', 'https://cdn-icons-png.flaticon.com/512/1046/1046801.png', 'Nước ép, sinh tố và các loại đồ uống', NOW(), NOW(), 'system', 'system', NULL, 0),
(19, 'Kem', 'https://cdn-icons-png.flaticon.com/512/1046/1046802.png', 'Kem tươi, kem Ý và các loại kem', NOW(), NOW(), 'system', 'system', NULL, 0),
(20, 'Đồ ăn vặt', 'https://cdn-icons-png.flaticon.com/512/1046/1046803.png', 'Bánh tráng trộn, nem nướng và các món ăn vặt', NOW(), NOW(), 'system', 'system', NULL, 0);

-- Restaurants (5 restaurants in Ho Chi Minh City)
INSERT INTO restaurants (id, code, name, description, address, phone, lat, lng, status, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
(1, 'REST001', 'Phở Hùng - Nguyễn Trãi', 'Phở bò truyền thống với nước dùng đậm đà, thịt bò tươi ngon. Quán phở nổi tiếng hơn 20 năm tại Sài Gòn.', '123 Nguyễn Trãi, Phường Nguyễn Cư Trinh, Quận 1, TP.HCM', '02838234567', 10.762622, 106.690172, 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(2, 'REST002', 'Cơm Tấm Cali - Lê Văn Việt', 'Cơm tấm Sài Gòn đặc trưng với sườn nướng thơm lừng, bì chả trứng. Không gian rộng rãi, phục vụ nhanh chóng.', '456 Lê Văn Việt, Phường Hiệp Phú, Quận 9, TP.HCM', '02837345678', 10.842622, 106.780172, 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 'REST003', 'Pizza Hut - Điện Biên Phủ', 'Pizza Ý chính thống với nhiều topping đa dạng. Phục vụ cả dine-in và delivery.', '789 Điện Biên Phủ, Phường 25, Bình Thạnh, TP.HCM', '02838456789', 10.802622, 106.710172, 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(4, 'REST004', 'KFC - Nguyễn Huệ', 'Gà rán giòn KFC với công thức đặc biệt. Menu đa dạng từ gà rán đến burger và đồ uống.', '321 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP.HCM', '02838567890', 10.772622, 106.700172, 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(5, 'REST005', 'Bún Bò Huế O Loan - Võ Văn Tần', 'Bún bò Huế đậm đà với nước dùng cay nồng đặc trưng xứ Huế. Thịt bò, chả cua tươi ngon.', '654 Võ Văn Tần, Phường 6, Quận 3, TP.HCM', '02838678901', 10.782622, 106.690172, 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0);

-- Restaurant Category Maps
INSERT INTO restaurant_category_maps (id, restaurant_id, category_id, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
(1, 1, 2, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(2, 2, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 2, 20, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(4, 3, 6, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(5, 3, 7, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(6, 3, 8, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(7, 4, 5, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(8, 4, 7, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(9, 5, 3, NOW(), NOW(), 'admin', 'admin', NULL, 0);

-- Restaurant Business Hours (7 days for each restaurant)
INSERT INTO restaurant_business_hours (id, restaurant_id, day_of_week, open_time, close_time, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
-- Restaurant 1 (Phở Hùng)
(1, 1, 0, '06:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(2, 1, 1, '06:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 1, 2, '06:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(4, 1, 3, '06:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(5, 1, 4, '06:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(6, 1, 5, '06:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(7, 1, 6, '06:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 2 (Cơm Tấm Cali) - Làm việc cả tuần bao gồm Chủ Nhật
(8, 2, 0, '10:00:00', '21:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0), -- Chủ Nhật
(9, 2, 1, '10:00:00', '21:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0), -- Thứ Hai
(10, 2, 2, '10:00:00', '21:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0), -- Thứ Ba
(11, 2, 3, '10:00:00', '21:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0), -- Thứ Tư
(12, 2, 4, '10:00:00', '21:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0), -- Thứ Năm
(13, 2, 5, '10:00:00', '21:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0), -- Thứ Sáu
(14, 2, 6, '10:00:00', '21:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0), -- Thứ Bảy
-- Restaurant 3 (Pizza Hut)
(15, 3, 0, '11:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(16, 3, 1, '11:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(17, 3, 2, '11:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(18, 3, 3, '11:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(19, 3, 4, '11:00:00', '22:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(20, 3, 5, '11:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(21, 3, 6, '11:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 4 (KFC)
(22, 4, 0, '07:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(23, 4, 1, '07:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(24, 4, 2, '07:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(25, 4, 3, '07:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(26, 4, 4, '07:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(27, 4, 5, '07:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(28, 4, 6, '07:00:00', '23:00:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 5 (Bún Bò Huế O Loan)
(29, 5, 0, '06:30:00', '21:30:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(30, 5, 1, '06:30:00', '21:30:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(31, 5, 2, '06:30:00', '21:30:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(32, 5, 3, '06:30:00', '21:30:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(33, 5, 4, '06:30:00', '21:30:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(34, 5, 5, '06:30:00', '21:30:00', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(35, 5, 6, '06:30:00', '21:30:00', NOW(), NOW(), 'admin', 'admin', NULL, 0);

-- Restaurant Preparation Settings
INSERT INTO restaurant_preparation_settings (id, restaurant_id, prep_time_min, prep_time_max, slot_duration, max_orders_per_slot, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
(1, 1, 15, 25, 15, 10, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(2, 2, 10, 20, 15, 15, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 3, 20, 35, 15, 8, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(4, 4, 12, 22, 15, 12, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(5, 5, 15, 25, 15, 10, NOW(), NOW(), 'admin', 'admin', NULL, 0);

-- Restaurant Ratings
INSERT INTO restaurant_ratings (id, restaurant_id, rating_avg, rating_count, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
(1, 1, 4.65, 1247, NOW(), NOW(), 'system', 'system', NULL, 0),
(2, 2, 4.52, 892, NOW(), NOW(), 'system', 'system', NULL, 0),
(3, 3, 4.38, 567, NOW(), NOW(), 'system', 'system', NULL, 0),
(4, 4, 4.42, 1834, NOW(), NOW(), 'system', 'system', NULL, 0),
(5, 5, 4.71, 1034, NOW(), NOW(), 'system', 'system', NULL, 0);

-- Restaurant Images
INSERT INTO restaurant_images (id, restaurant_id, image_url, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
-- Restaurant 1 (5 images)
(1, 1, 'https://images.unsplash.com/photo-1529042410759-befb1204b468', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(2, 1, 'https://images.unsplash.com/photo-1551218808-94e220e084d2', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 1, 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(4, 1, 'https://images.unsplash.com/photo-1558030006-450675393462', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(5, 1, 'https://images.unsplash.com/photo-1559339352-11d035aa65de', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 2 (4 images)
(6, 2, 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(7, 2, 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(8, 2, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(9, 2, 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 3 (6 images)
(10, 3, 'https://images.unsplash.com/photo-1513104890138-7c749659a591', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(11, 3, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(12, 3, 'https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(13, 3, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(14, 3, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(15, 3, 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 4 (3 images)
(16, 4, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(17, 4, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(18, 4, 'https://images.unsplash.com/photo-1606755962773-d324e0a13086', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 5 (5 images)
(19, 5, 'https://images.unsplash.com/photo-1551218808-94e220e084d2', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(20, 5, 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(21, 5, 'https://images.unsplash.com/photo-1558030006-450675393462', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(22, 5, 'https://images.unsplash.com/photo-1559339352-11d035aa65de', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(23, 5, 'https://images.unsplash.com/photo-1529042410759-befb1204b468', NOW(), NOW(), 'admin', 'admin', NULL, 0);

-- Menus
INSERT INTO menus (id, restaurant_id, name, start_time, end_time, status, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
-- Restaurant 1: 1 menu (all day)
(1, 1, 'Menu Chính', '06:00:00', '22:00:00', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 2: 2 menus
(2, 2, 'Menu Sáng', '10:00:00', '14:00:00', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 2, 'Menu Chiều', '14:00:00', '21:00:00', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 3: 1 menu
(4, 3, 'Menu Chính', '11:00:00', '23:00:00', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 4: 2 menus
(5, 4, 'Menu Sáng', '07:00:00', '11:00:00', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
(6, 4, 'Menu Chính', '11:00:00', '23:00:00', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Restaurant 5: 1 menu
(7, 5, 'Menu Chính', '06:30:00', '21:30:00', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0);

-- Menu Items
INSERT INTO menu_items (id, menu_id, name, description, price, image_url, status, created_at, updated_at, created_by, updated_by, deleted_at, version, restaurant_id) VALUES
-- Restaurant 1 - Menu 1 (8 items)
(1, 1, 'Phở Bò Tái', 'Phở bò với thịt bò tái chín vừa, nước dùng đậm đà, hành lá, rau thơm', 75000.00, 'https://images.unsplash.com/photo-1529042410759-befb1204b468', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
(2, 1, 'Phở Bò Chín', 'Phở bò với thịt bò chín mềm, nước dùng thơm ngon', 75000.00, 'https://images.unsplash.com/photo-1551218808-94e220e084d2', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
(3, 1, 'Phở Bò Tái Chín', 'Phở bò kết hợp cả tái và chín, đầy đủ hương vị', 85000.00, 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
(4, 1, 'Phở Bò Viên', 'Phở bò với bò viên thơm ngon, dai giòn', 70000.00, 'https://images.unsplash.com/photo-1558030006-450675393462', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
(5, 1, 'Phở Đặc Biệt', 'Phở đầy đủ thịt bò tái, chín, viên, gân, sách', 95000.00, 'https://images.unsplash.com/photo-1559339352-11d035aa65de', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
(6, 1, 'Phở Gà', 'Phở gà với thịt gà luộc thơm, nước dùng trong', 70000.00, 'https://images.unsplash.com/photo-1529042410759-befb1204b468', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
(7, 1, 'Phở Bò Gân', 'Phở bò với gân bò mềm ngon, giàu collagen', 80000.00, 'https://images.unsplash.com/photo-1551218808-94e220e084d2', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
(8, 1, 'Phở Bò Sách', 'Phở bò với sách bò giòn dai, đặc biệt', 80000.00, 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 1),
-- Restaurant 2 - Menu 2 (7 items)
(9, 2, 'Cơm Tấm Sườn Nướng', 'Cơm tấm với sườn nướng thơm lừng, bì chả trứng', 65000.00, 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(10, 2, 'Cơm Tấm Sườn Bì Chả', 'Cơm tấm đầy đủ sườn, bì, chả, trứng', 75000.00, 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(11, 2, 'Cơm Tấm Đặc Biệt', 'Cơm tấm với đầy đủ sườn, bì, chả, trứng, chả trứng hấp', 85000.00, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(12, 2, 'Cơm Tấm Chả Trứng', 'Cơm tấm với chả trứng thơm ngon', 55000.00, 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(13, 2, 'Cơm Tấm Gà Nướng', 'Cơm tấm với gà nướng mật ong thơm lừng', 70000.00, 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(14, 2, 'Cơm Tấm Thịt Nướng', 'Cơm tấm với thịt nướng đậm đà', 65000.00, 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(15, 2, 'Cơm Tấm Chả Cá', 'Cơm tấm với chả cá thơm ngon', 60000.00, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
-- Restaurant 2 - Menu 3 (6 items)
(16, 3, 'Cơm Tấm Sườn Nướng', 'Cơm tấm với sườn nướng thơm lừng, bì chả trứng', 65000.00, 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(17, 3, 'Cơm Tấm Sườn Bì Chả', 'Cơm tấm đầy đủ sườn, bì, chả, trứng', 75000.00, 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(18, 3, 'Cơm Tấm Đặc Biệt', 'Cơm tấm với đầy đủ sườn, bì, chả, trứng, chả trứng hấp', 85000.00, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(19, 3, 'Cơm Tấm Gà Nướng', 'Cơm tấm với gà nướng mật ong thơm lừng', 70000.00, 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(20, 3, 'Cơm Tấm Thịt Nướng', 'Cơm tấm với thịt nướng đậm đà', 65000.00, 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
(21, 3, 'Cơm Tấm Chả Cá', 'Cơm tấm với chả cá thơm ngon', 60000.00, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 2),
-- Restaurant 3 - Menu 4 (9 items)
(22, 4, 'Pizza Margherita', 'Pizza cổ điển với phô mai mozzarella, cà chua tươi, lá basil', 199000.00, 'https://images.unsplash.com/photo-1513104890138-7c749659a591', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(23, 4, 'Pizza Pepperoni', 'Pizza với pepperoni cay, phô mai mozzarella', 229000.00, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(24, 4, 'Pizza Hải Sản', 'Pizza với tôm, mực, cua, phô mai mozzarella', 279000.00, 'https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(25, 4, 'Pizza 4 Phô Mai', 'Pizza với 4 loại phô mai đặc biệt', 249000.00, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(26, 4, 'Pizza Thịt Nướng', 'Pizza với thịt nướng BBQ, hành tây, ớt chuông', 259000.00, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(27, 4, 'Burger Bò Phô Mai', 'Burger với thịt bò nướng, phô mai, rau sống', 129000.00, 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(28, 4, 'Burger Gà', 'Burger với gà rán giòn, rau sống, sốt đặc biệt', 119000.00, 'https://images.unsplash.com/photo-1513104890138-7c749659a591', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(29, 4, 'Spaghetti Carbonara', 'Mì Ý với sốt kem, thịt xông khói, phô mai', 149000.00, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
(30, 4, 'Spaghetti Bolognese', 'Mì Ý với sốt thịt bò băm, cà chua', 139000.00, 'https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 3),
-- Restaurant 4 - Menu 5 (6 items)
(31, 5, 'Gà Rán Giòn 2 Miếng', '2 miếng gà rán giòn cay, khoai tây chiên', 89000.00, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(32, 5, 'Gà Rán Giòn 3 Miếng', '3 miếng gà rán giòn cay, khoai tây chiên', 119000.00, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(33, 5, 'Combo Gà Rán 2 Miếng', '2 miếng gà, khoai tây chiên, pepsi', 109000.00, 'https://images.unsplash.com/photo-1606755962773-d324e0a13086', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(34, 5, 'Combo Gà Rán 3 Miếng', '3 miếng gà, khoai tây chiên, pepsi', 139000.00, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(35, 5, 'Gà Rán Không Cay', 'Gà rán giòn không cay, khoai tây chiên', 89000.00, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(36, 5, 'Khoai Tây Chiên', 'Khoai tây chiên giòn vàng', 35000.00, 'https://images.unsplash.com/photo-1606755962773-d324e0a13086', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
-- Restaurant 4 - Menu 6 (8 items)
(37, 6, 'Gà Rán Giòn 2 Miếng', '2 miếng gà rán giòn cay, khoai tây chiên', 89000.00, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(38, 6, 'Gà Rán Giòn 3 Miếng', '3 miếng gà rán giòn cay, khoai tây chiên', 119000.00, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(39, 6, 'Combo Gà Rán 2 Miếng', '2 miếng gà, khoai tây chiên, pepsi', 109000.00, 'https://images.unsplash.com/photo-1606755962773-d324e0a13086', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(40, 6, 'Combo Gà Rán 3 Miếng', '3 miếng gà, khoai tây chiên, pepsi', 139000.00, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(41, 6, 'Burger Zinger', 'Burger gà rán giòn cay, rau sống, sốt đặc biệt', 99000.00, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(42, 6, 'Burger Bò Phô Mai', 'Burger thịt bò nướng, phô mai, rau sống', 109000.00, 'https://images.unsplash.com/photo-1606755962773-d324e0a13086', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(43, 6, 'Gà Rán Không Cay', 'Gà rán giòn không cay, khoai tây chiên', 89000.00, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
(44, 6, 'Khoai Tây Chiên', 'Khoai tây chiên giòn vàng', 35000.00, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 4),
-- Restaurant 5 - Menu 7 (7 items)
(45, 7, 'Bún Bò Huế Đặc Biệt', 'Bún bò Huế đầy đủ thịt bò, chả cua, giò heo, máu', 85000.00, 'https://images.unsplash.com/photo-1551218808-94e220e084d2', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 5),
(46, 7, 'Bún Bò Huế Thường', 'Bún bò Huế với thịt bò, chả cua', 65000.00, 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 5),
(47, 7, 'Bún Bò Huế Chả Cua', 'Bún bò Huế với chả cua thơm ngon', 70000.00, 'https://images.unsplash.com/photo-1558030006-450675393462', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 5),
(48, 7, 'Bún Bò Huế Giò Heo', 'Bún bò Huế với giò heo mềm ngon', 75000.00, 'https://images.unsplash.com/photo-1559339352-11d035aa65de', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 5),
(49, 7, 'Bún Bò Huế Không Cay', 'Bún bò Huế không cay cho người không ăn được cay', 65000.00, 'https://images.unsplash.com/photo-1529042410759-befb1204b468', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 5),
(50, 7, 'Bún Bò Huế Thêm Thịt', 'Bún bò Huế với phần thịt bò gấp đôi', 95000.00, 'https://images.unsplash.com/photo-1551218808-94e220e084d2', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 5),
(51, 7, 'Bún Bò Huế Chay', 'Bún bò Huế chay với nấm và rau củ', 55000.00, 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5', 'ACTIVE', NOW(), NOW(), 'admin', 'admin', NULL, 0, 5);

-- Menu Item Options
INSERT INTO menu_item_options (id, menu_item_id, name, required, min_value, max_value, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
-- Pizza items have size options
(1, 22, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(2, 23, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 24, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(4, 25, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(5, 26, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Burger items have extra options
(6, 27, 'Thêm topping', 0, 0, 3, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(7, 28, 'Thêm topping', 0, 0, 3, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(8, 41, 'Thêm topping', 0, 0, 3, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(9, 42, 'Thêm topping', 0, 0, 3, NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Cơm tấm items have size options
(10, 9, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(11, 10, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(12, 11, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(13, 16, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(14, 17, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(15, 18, 'Kích thước', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Bún bò Huế items have spice level
(16, 45, 'Độ cay', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(17, 46, 'Độ cay', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(18, 47, 'Độ cay', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(19, 48, 'Độ cay', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(20, 50, 'Độ cay', 1, 1, 1, NOW(), NOW(), 'admin', 'admin', NULL, 0);

-- Menu Item Option Values
INSERT INTO menu_item_option_values (id, option_id, name, extra_price, created_at, updated_at, created_by, updated_by, deleted_at, version) VALUES
-- Pizza sizes
(1, 1, 'Nhỏ (20cm)', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(2, 1, 'Vừa (25cm)', 50000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(3, 1, 'Lớn (30cm)', 100000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(4, 2, 'Nhỏ (20cm)', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(5, 2, 'Vừa (25cm)', 50000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(6, 2, 'Lớn (30cm)', 100000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(7, 3, 'Nhỏ (20cm)', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(8, 3, 'Vừa (25cm)', 50000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(9, 3, 'Lớn (30cm)', 100000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(10, 4, 'Nhỏ (20cm)', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(11, 4, 'Vừa (25cm)', 50000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(12, 4, 'Lớn (30cm)', 100000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(13, 5, 'Nhỏ (20cm)', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(14, 5, 'Vừa (25cm)', 50000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(15, 5, 'Lớn (30cm)', 100000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Burger toppings
(16, 6, 'Thêm phô mai', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(17, 6, 'Thêm thịt xông khói', 20000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(18, 6, 'Thêm trứng', 10000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(19, 6, 'Thêm nấm', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(20, 7, 'Thêm phô mai', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(21, 7, 'Thêm thịt xông khói', 20000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(22, 7, 'Thêm trứng', 10000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(23, 7, 'Thêm nấm', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(24, 8, 'Thêm phô mai', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(25, 8, 'Thêm thịt xông khói', 20000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(26, 8, 'Thêm trứng', 10000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(27, 8, 'Thêm nấm', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(28, 9, 'Thêm phô mai', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(29, 9, 'Thêm thịt xông khói', 20000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(30, 9, 'Thêm trứng', 10000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(31, 9, 'Thêm nấm', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Cơm tấm sizes
(32, 10, 'Phần thường', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(33, 10, 'Phần lớn', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(34, 11, 'Phần thường', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(35, 11, 'Phần lớn', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(36, 12, 'Phần thường', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(37, 12, 'Phần lớn', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(38, 13, 'Phần thường', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(39, 13, 'Phần lớn', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(40, 14, 'Phần thường', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(41, 14, 'Phần lớn', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(42, 15, 'Phần thường', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(43, 15, 'Phần lớn', 15000.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
-- Bún bò Huế spice levels
(44, 16, 'Không cay', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(45, 16, 'Cay nhẹ', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(46, 16, 'Cay vừa', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(47, 16, 'Cay nhiều', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(48, 16, 'Cay cực', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(49, 17, 'Không cay', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(50, 17, 'Cay nhẹ', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(51, 17, 'Cay vừa', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(52, 17, 'Cay nhiều', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(53, 17, 'Cay cực', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(54, 18, 'Không cay', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(55, 18, 'Cay nhẹ', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(56, 18, 'Cay vừa', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(57, 18, 'Cay nhiều', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(58, 18, 'Cay cực', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(59, 19, 'Không cay', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(60, 19, 'Cay nhẹ', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(61, 19, 'Cay vừa', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(62, 19, 'Cay nhiều', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(63, 19, 'Cay cực', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(64, 20, 'Không cay', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(65, 20, 'Cay nhẹ', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(66, 20, 'Cay vừa', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(67, 20, 'Cay nhiều', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0),
(68, 20, 'Cay cực', 0.00, NOW(), NOW(), 'admin', 'admin', NULL, 0);
