-- Add iPhone products to the product catalog service database

USE product_catalog_service;

-- Insert iPhone category if it doesn't exist
INSERT IGNORE INTO category (name, description, created_at, last_updated_at, state) 
VALUES ('Smartphones', 'Mobile phones and smartphones', NOW(), NOW(), 1);

-- Get the category ID for Smartphones
SET @smartphone_category_id = (SELECT id FROM category WHERE name = 'Smartphones' LIMIT 1);

-- Insert iPhone products
INSERT INTO product (name, description, price, image_url, category_id, created_at, last_updated_at, state) VALUES
('iPhone 15 Pro Max', 'iPhone 15 Pro Max with A17 Pro chip, titanium design, and advanced camera system. 256GB storage.', 1199.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-pro-max-blue-select?wid=470&hei=556&fmt=png-alpha&.v=1693015216657', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 15 Pro', 'iPhone 15 Pro with A17 Pro chip, titanium design, and pro camera system. 128GB storage.', 999.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-pro-finish-select-202309-6-1inch-blue?wid=2560&hei=1440&fmt=png-alpha&.v=1692925578451', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 15 Plus', 'iPhone 15 Plus with Dynamic Island, 48MP Main camera, and all-day battery life. 128GB storage.', 899.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-plus-blue-select?wid=470&hei=556&fmt=png-alpha&.v=1693015218657', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 15', 'iPhone 15 with Dynamic Island, 48MP Main camera, and all-day battery life. 128GB storage.', 799.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-blue-select?wid=470&hei=556&fmt=png-alpha&.v=1693015216657', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 14 Pro Max', 'iPhone 14 Pro Max with A16 Bionic chip, Dynamic Island, and Pro camera system. 256GB storage.', 1099.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-14-pro-max-deep-purple-select?wid=470&hei=556&fmt=png-alpha&.v=1692875665457', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 14 Pro', 'iPhone 14 Pro with A16 Bionic chip, Dynamic Island, and Pro camera system. 128GB storage.', 999.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-14-pro-deep-purple-select?wid=470&hei=556&fmt=png-alpha&.v=1692875665457', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 14 Plus', 'iPhone 14 Plus with A15 Bionic chip, advanced dual-camera system, and all-day battery life. 128GB storage.', 899.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-14-plus-blue-select?wid=470&hei=556&fmt=png-alpha&.v=1692925578451', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 14', 'iPhone 14 with A15 Bionic chip, advanced dual-camera system, and all-day battery life. 128GB storage.', 799.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-14-blue-select?wid=470&hei=556&fmt=png-alpha&.v=1692925578451', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone SE (3rd generation)', 'iPhone SE with A15 Bionic chip, Touch ID, and advanced camera system. 64GB storage.', 429.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-se-midnight-select?wid=470&hei=556&fmt=png-alpha&.v=1692875665457', @smartphone_category_id, NOW(), NOW(), 1),

('iPhone 13', 'iPhone 13 with A15 Bionic chip, advanced dual-camera system, and all-day battery life. 128GB storage.', 699.99, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-13-blue-select?wid=470&hei=556&fmt=png-alpha&.v=1692875665457', @smartphone_category_id, NOW(), NOW(), 1);

-- Display the inserted products
SELECT p.name, p.description, p.price, c.name as category 
FROM product p 
JOIN category c ON p.category_id = c.id 
WHERE c.name = 'Smartphones' 
ORDER BY p.price DESC;
