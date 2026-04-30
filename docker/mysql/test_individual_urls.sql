-- Test with different working image URLs for each phone

-- Test with PlaceIMG (alternative working service)
UPDATE product SET image_url = 'https://placeimg.com/400/400/tech' 
WHERE id = 1 AND name LIKE '%iPhone 15 Pro Max%';

UPDATE product SET image_url = 'https://placeimg.com/400/400/tech' 
WHERE id = 2 AND name LIKE '%Samsung Galaxy S24 Ultra%';

UPDATE product SET image_url = 'https://placeimg.com/400/400/tech' 
WHERE id = 3 AND name LIKE '%Google Pixel 8 Pro%';

UPDATE product SET image_url = 'https://placeimg.com/400/400/tech' 
WHERE id = 4 AND name LIKE '%OnePlus 12%';

UPDATE product SET image_url = 'https://placeimg.com/400/400/tech' 
WHERE id = 6 AND name LIKE '%iPhone 14%';

UPDATE product SET image_url = 'https://placeimg.com/400/400/tech' 
WHERE id = 7 AND name LIKE '%Samsung Galaxy Z Fold5%';

UPDATE product SET image_url = 'https://placeimg.com/400/400/tech' 
WHERE id = 9 AND name LIKE '%Nothing Phone 2%';

-- Test the duplicates with different service
UPDATE product SET image_url = 'https://image.placeholder.com/400x400/007bff/ffffff?text=iPhone+15+Pro+Max' 
WHERE id = 101 AND name LIKE '%iPhone 15 Pro Max%';

UPDATE product SET image_url = 'https://image.placeholder.com/400x400/dc3545/ffffff?text=Galaxy+S24+Ultra' 
WHERE id = 102 AND name LIKE '%Samsung Galaxy S24 Ultra%';

UPDATE product SET image_url = 'https://image.placeholder.com/400x400/17a2b8/ffffff?text=Pixel+8+Pro' 
WHERE id = 103 AND name LIKE '%Google Pixel 8 Pro%';

UPDATE product SET image_url = 'https://image.placeholder.com/400x400/6f42c1/ffffff?text=OnePlus+12' 
WHERE id = 104 AND name LIKE '%OnePlus 12%';

UPDATE product SET image_url = 'https://image.placeholder.com/400x400/28a745/ffffff?text=iPhone+14' 
WHERE id = 106 AND name LIKE '%iPhone 14%';

UPDATE product SET image_url = 'https://image.placeholder.com/400x400/ffc107/000000?text=Galaxy+Z+Fold5' 
WHERE id = 107 AND name LIKE '%Samsung Galaxy Z Fold5%';

UPDATE product SET image_url = 'https://image.placeholder.com/400x400/e83e8c/000000?text=Nothing+Phone+2' 
WHERE id = 109 AND name LIKE '%Nothing Phone 2%';

-- Verify updates
SELECT id, name, image_url FROM product 
WHERE name LIKE '%phone%' OR name LIKE '%iPhone%' OR name LIKE '%Samsung%' OR name LIKE '%Nothing%' OR name LIKE '%OnePlus%' OR name LIKE '%Google%' OR name LIKE '%Pixel%'
AND name NOT LIKE '%Galaxy Book%' AND name NOT LIKE '%Neo QLED%' AND name NOT LIKE '%Soundbar%' AND name NOT LIKE '%Convection MW%'
ORDER BY id;
