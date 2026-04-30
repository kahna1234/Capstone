-- Fix phone product images with correct, unique Unsplash URLs

-- iPhone 15 Pro Max - Real iPhone image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1592750475338-74b7bb210bab?w=400&h=400&fit=crop' 
WHERE name LIKE '%iPhone 15 Pro Max%';

-- iPhone 14 - Real iPhone image  
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1592286115803-a1c2b5520c4e?w=400&h=400&fit=crop' 
WHERE name LIKE '%iPhone 14%';

-- Samsung Galaxy S24 Ultra - Real Samsung phone
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1580910051474-3e22b29ebb03?w=400&h=400&fit=crop' 
WHERE name LIKE '%Samsung Galaxy S24 Ultra%';

-- Samsung Galaxy Z Fold5 - Foldable phone image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1610945415495-0a298e1d57f1?w=400&h=400&fit=crop' 
WHERE name LIKE '%Samsung Galaxy Z Fold5%';

-- Google Pixel 8 Pro - Real Pixel phone
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1605236453806-b63301a293dd?w=400&h=400&fit=crop' 
WHERE name LIKE '%Google Pixel 8 Pro%';

-- OnePlus 12 - Real OnePlus phone
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=400&fit=crop' 
WHERE name LIKE '%OnePlus 12%';

-- Nothing Phone 2 - Real Nothing phone (different image)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1572569511254-398e2985c5f5?w=400&h=400&fit=crop' 
WHERE name LIKE '%Nothing Phone 2%';

-- Verify updates
SELECT id, name, image_url FROM product 
WHERE name LIKE '%phone%' OR name LIKE '%iPhone%' OR name LIKE '%Samsung%' OR name LIKE '%Nothing%' OR name LIKE '%OnePlus%' OR name LIKE '%Google%' OR name LIKE '%Pixel%'
AND name NOT LIKE '%Galaxy Book%' AND name NOT LIKE '%Neo QLED%' AND name NOT LIKE '%Soundbar%' AND name NOT LIKE '%Convection MW%'
ORDER BY id;
