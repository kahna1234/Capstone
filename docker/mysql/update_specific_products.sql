-- Update specific products with provided Unsplash URLs

-- Apple iPhone 15 Pro Max 256GB
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600' 
WHERE name = 'Apple iPhone 15 Pro Max 256GB';

-- Samsung Galaxy S24 Ultra 512GB
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600' 
WHERE name = 'Samsung Galaxy S24 Ultra 512GB';

-- Google Pixel 8 Pro 128GB
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600' 
WHERE name = 'Google Pixel 8 Pro 128GB';

-- OnePlus 12 256GB
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1580910051074-3eb694886505?w=600' 
WHERE name = 'OnePlus 12 256GB';

-- Apple MacBook Pro 14-inch M3
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600' 
WHERE name = 'Apple MacBook Pro 14-inch M3';

-- Dell XPS 15 OLED Laptop
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600' 
WHERE name = 'Dell XPS 15 OLED Laptop';

-- Samsung 65" Neo QLED 4K TV
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1593784991095-a205069470b6?w=600' 
WHERE name = 'Samsung 65" Neo QLED 4K TV';

-- Sony WH-1000XM5 Wireless
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1518443895914-8f5cdbd1f1f8?w=600' 
WHERE name = 'Sony WH-1000XM5 Wireless';

-- Apple AirPods Pro 2nd Gen
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1588423771073-b8903fbb85b5?w=600' 
WHERE name = 'Apple AirPods Pro 2nd Gen';

-- Sony Alpha 7 IV Mirrorless
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1519183071298-a2962eadc8a4?w=600' 
WHERE name = 'Sony Alpha 7 IV Mirrorless';

-- Dyson V15 Detect Vacuum
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=600' 
WHERE name = 'Dyson V15 Detect Vacuum';

-- Nike Air Jordan 1 Retro
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=600' 
WHERE name = 'Nike Air Jordan 1 Retro';

-- Atomic Habits
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600' 
WHERE name = 'Atomic Habits by James Clear';

-- LEGO Technic Ferrari
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=600' 
WHERE name = 'LEGO Technic Ferrari';

-- IKEA POÄNG Armchair
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=600' 
WHERE name = 'IKEA POÄNG Armchair';

-- Verify updates
SELECT id, name, price, image_url FROM product 
WHERE name IN (
    'Apple iPhone 15 Pro Max 256GB',
    'Samsung Galaxy S24 Ultra 512GB', 
    'Google Pixel 8 Pro 128GB',
    'OnePlus 12 256GB',
    'Apple MacBook Pro 14-inch M3',
    'Dell XPS 15 OLED Laptop',
    'Samsung 65" Neo QLED 4K TV',
    'Sony WH-1000XM5 Wireless',
    'Apple AirPods Pro 2nd Gen',
    'Sony Alpha 7 IV Mirrorless',
    'Dyson V15 Detect Vacuum',
    'Nike Air Jordan 1 Retro',
    'Atomic Habits by James Clear',
    'LEGO Technic Ferrari',
    'IKEA POÄNG Armchair'
)
ORDER BY id;
