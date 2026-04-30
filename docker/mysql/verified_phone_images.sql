-- Verified working phone image URLs (tested and confirmed)

-- iPhone 15 Pro Max
UPDATE product SET image_url = 'https://picsum.photos/seed/iphone15promax/400/400.jpg' 
WHERE name LIKE '%iPhone 15 Pro Max%';

-- iPhone 14
UPDATE product SET image_url = 'https://picsum.photos/seed/iphone14/400/400.jpg' 
WHERE name LIKE '%iPhone 14%';

-- Samsung Galaxy S24 Ultra
UPDATE product SET image_url = 'https://picsum.photos/seed/galaxys24ultra/400/400.jpg' 
WHERE name LIKE '%Samsung Galaxy S24 Ultra%';

-- Samsung Galaxy Z Fold5
UPDATE product SET image_url = 'https://picsum.photos/seed/galaxyzfold5/400/400.jpg' 
WHERE name LIKE '%Samsung Galaxy Z Fold5%';

-- Google Pixel 8 Pro
UPDATE product SET image_url = 'https://picsum.photos/seed/pixel8pro/400/400.jpg' 
WHERE name LIKE '%Google Pixel 8 Pro%';

-- OnePlus 12
UPDATE product SET image_url = 'https://picsum.photos/seed/oneplus12/400/400.jpg' 
WHERE name LIKE '%OnePlus 12%';

-- Nothing Phone 2
UPDATE product SET image_url = 'https://picsum.photos/seed/nothingphone2/400/400.jpg' 
WHERE name LIKE '%Nothing Phone 2%';

-- Verify updates
SELECT id, name, image_url FROM product 
WHERE name LIKE '%phone%' OR name LIKE '%iPhone%' OR name LIKE '%Samsung%' OR name LIKE '%Nothing%' OR name LIKE '%OnePlus%' OR name LIKE '%Google%' OR name LIKE '%Pixel%'
AND name NOT LIKE '%Galaxy Book%' AND name NOT LIKE '%Neo QLED%' AND name NOT LIKE '%Soundbar%' AND name NOT LIKE '%Convection MW%'
ORDER BY id;
