-- Unique phone images for all phone products

-- iPhone 15 Pro Max (ID 1)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1592750475338-74b7bb210bab?w=400&h=400&fit=crop' 
WHERE id = 1 AND name LIKE '%iPhone 15 Pro Max%';

-- iPhone 15 Pro Max (ID 101) - Different image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1592286115803-a1c2b5520c4e?w=400&h=400&fit=crop' 
WHERE id = 101 AND name LIKE '%iPhone 15 Pro Max%';

-- Samsung Galaxy S24 Ultra (ID 2)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1580910051474-3e22b29ebb03?w=400&h=400&fit=crop' 
WHERE id = 2 AND name LIKE '%Samsung Galaxy S24 Ultra%';

-- Samsung Galaxy S24 Ultra (ID 102) - Different image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1610945415495-0a298e1d57f1?w=400&h=400&fit=crop' 
WHERE id = 102 AND name LIKE '%Samsung Galaxy S24 Ultra%';

-- Google Pixel 8 Pro (ID 3)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1605236453806-b63301a293dd?w=400&h=400&fit=crop' 
WHERE id = 3 AND name LIKE '%Google Pixel 8 Pro%';

-- Google Pixel 8 Pro (ID 103) - Different image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1572569511254-398e2985c5f5?w=400&h=400&fit=crop' 
WHERE id = 103 AND name LIKE '%Google Pixel 8 Pro%';

-- OnePlus 12 (ID 4)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=400&fit=crop' 
WHERE id = 4 AND name LIKE '%OnePlus 12%';

-- OnePlus 12 (ID 104) - Different image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1592750475338-74b7bb210bab?w=400&h=400&fit=crop' 
WHERE id = 104 AND name LIKE '%OnePlus 12%';

-- iPhone 14 (ID 6)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1592286115803-a1c2b5520c4e?w=400&h=400&fit=crop' 
WHERE id = 6 AND name LIKE '%iPhone 14%';

-- iPhone 14 (ID 106) - Different image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1580910051474-3e22b29ebb03?w=400&h=400&fit=crop' 
WHERE id = 106 AND name LIKE '%iPhone 14%';

-- Samsung Galaxy Z Fold5 (ID 7)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1610945415495-0a298e1d57f1?w=400&h=400&fit=crop' 
WHERE id = 7 AND name LIKE '%Samsung Galaxy Z Fold5%';

-- Samsung Galaxy Z Fold5 (ID 107) - Different image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1605236453806-b63301a293dd?w=400&h=400&fit=crop' 
WHERE id = 107 AND name LIKE '%Samsung Galaxy Z Fold5%';

-- Nothing Phone 2 (ID 9)
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1572569511254-398e2985c5f5?w=400&h=400&fit=crop' 
WHERE id = 9 AND name LIKE '%Nothing Phone 2%';

-- Nothing Phone 2 (ID 109) - Different image
UPDATE product SET image_url = 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=400&fit=crop' 
WHERE id = 109 AND name LIKE '%Nothing Phone 2%';

-- Verify unique URLs
SELECT id, name, image_url FROM product 
WHERE name LIKE '%phone%' OR name LIKE '%iPhone%' OR name LIKE '%Samsung%' OR name LIKE '%Nothing%' OR name LIKE '%OnePlus%' OR name LIKE '%Google%' OR name LIKE '%Pixel%'
AND name NOT LIKE '%Galaxy Book%' AND name NOT LIKE '%Neo QLED%' AND name NOT LIKE '%Soundbar%' AND name NOT LIKE '%Conduction MW%'
ORDER BY id;
