-- Test with verified working image URLs

-- Using DummyImage (guaranteed to work)
UPDATE product SET image_url = 'https://dummyimage.com/400x400/007bff/ffffff&text=iPhone+15+Pro+Max' 
WHERE name LIKE '%iPhone 15 Pro Max%';

UPDATE product SET image_url = 'https://dummyimage.com/400x400/28a745/ffffff&text=iPhone+14' 
WHERE name LIKE '%iPhone 14%';

UPDATE product SET image_url = 'https://dummyimage.com/400x400/dc3545/ffffff&text=Galaxy+S24+Ultra' 
WHERE name LIKE '%Samsung Galaxy S24 Ultra%';

UPDATE product SET image_url = 'https://dummyimage.com/400x400/ffc107/000000&text=Galaxy+Z+Fold5' 
WHERE name LIKE '%Samsung Galaxy Z Fold5%';

UPDATE product SET image_url = 'https://dummyimage.com/400x400/17a2b8/ffffff&text=Pixel+8+Pro' 
WHERE name LIKE '%Google Pixel 8 Pro%';

UPDATE product SET image_url = 'https://dummyimage.com/400x400/6f42c1/ffffff&text=OnePlus+12' 
WHERE name LIKE '%OnePlus 12%';

UPDATE product SET image_url = 'https://dummyimage.com/400x400/e83e8c/000000&text=Nothing+Phone+2' 
WHERE name LIKE '%Nothing Phone 2%';

-- Verify updates
SELECT id, name, image_url FROM product 
WHERE name LIKE '%phone%' OR name LIKE '%iPhone%' OR name LIKE '%Samsung%' OR name LIKE '%Nothing%' OR name LIKE '%OnePlus%' OR name LIKE '%Google%' OR name LIKE '%Pixel%'
AND name NOT LIKE '%Galaxy Book%' AND name NOT LIKE '%Neo QLED%' AND name NOT LIKE '%Soundbar%' AND name NOT LIKE '%Convection MW%'
ORDER BY id;
