-- Update phone products with verified Pexels image URLs

-- iPhone 15 Pro Max - Real iPhone photo
UPDATE product SET image_url = 'https://images.pexels.com/photos/807903/pexels-photo-807903.jpeg?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop' 
WHERE name LIKE '%iPhone 15 Pro Max%';

-- iPhone 14 - Real iPhone photo
UPDATE product SET image_url = 'https://images.pexels.com/photos/518194/pexels-photo-518194.jpeg?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop' 
WHERE name LIKE '%iPhone 14%';

-- Samsung Galaxy S24 Ultra - Real Samsung phone
UPDATE product SET image_url = 'https://images.pexels.com/photos/265658/pexels-photo-265658.jpeg?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop' 
WHERE name LIKE '%Samsung Galaxy S24 Ultra%';

-- Samsung Galaxy Z Fold5 - Foldable phone
UPDATE product SET image_url = 'https://images.pexels.com/photos/1810525/pexels-photo-1810525.jpeg?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop' 
WHERE name LIKE '%Samsung Galaxy Z Fold5%';

-- Google Pixel 8 Pro - Real Pixel phone
UPDATE product SET image_url = 'https://images.pexels.com/photos/4098429/pexels-photo-4098429.jpeg?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop' 
WHERE name LIKE '%Google Pixel 8 Pro%';

-- OnePlus 12 - Real OnePlus phone
UPDATE product SET image_url = 'https://images.pexels.com/photos/326503/pexels-photo-326503.jpeg?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop' 
WHERE name LIKE '%OnePlus 12%';

-- Nothing Phone 2 - Modern smartphone
UPDATE product SET image_url = 'https://images.pexels.com/photos/7974/pexels-photo-7974.jpeg?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop' 
WHERE name LIKE '%Nothing Phone 2%';

-- Verify updates
SELECT id, name, image_url FROM product 
WHERE name LIKE '%phone%' OR name LIKE '%iPhone%' OR name LIKE '%Samsung%' OR name LIKE '%Nothing%' OR name LIKE '%OnePlus%' OR name LIKE '%Google%' OR name LIKE '%Pixel%'
AND name NOT LIKE '%Galaxy Book%' AND name NOT LIKE '%Neo QLED%' AND name NOT LIKE '%Soundbar%' AND name NOT LIKE '%Conduction MW%'
ORDER BY id;
