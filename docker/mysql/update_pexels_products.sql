-- Update specific products with Pexels URLs

-- PHONES & LAPTOPS
UPDATE product SET image_url = 'https://images.pexels.com/photos/788946/pexels-photo-788946.jpeg' 
WHERE name LIKE '%Apple iPhone 14%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/18105/pexels-photo.jpg' 
WHERE name LIKE '%HP Spectre x360%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/18105/pexels-photo.jpg' 
WHERE name LIKE '%Samsung Galaxy Book3 Pro%';

-- ELECTRONICS
UPDATE product SET image_url = 'https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg' 
WHERE name LIKE '%Bose QuietComfort Ultra%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg' 
WHERE name LIKE '%Sony WH-1000XM5%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/1649771/pexels-photo-1649771.jpeg' 
WHERE name LIKE '%Samsung HW-Q990C Soundbar%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/1649771/pexels-photo-1649771.jpeg' 
WHERE name LIKE '%JBL Flip 6%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/1649771/pexels-photo-1649771.jpeg' 
WHERE name LIKE '%Sonos Era 300%';

-- CAMERAS
UPDATE product SET image_url = 'https://images.pexels.com/photos/51383/photo-camera-subject-photographer-51383.jpeg' 
WHERE name LIKE '%Sony Alpha 7 IV Mirrorless%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/51383/photo-camera-subject-photographer-51383.jpeg' 
WHERE name LIKE '%Fujifilm X-T5 Mirrorless%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/442587/pexels-photo-442587.jpeg' 
WHERE name LIKE '%DJI Mini 4 Pro Drone%';

-- HOME APPLIANCES
UPDATE product SET image_url = 'https://images.pexels.com/photos/4239031/pexels-photo-4239031.jpeg' 
WHERE name LIKE '%LG 7kg Front Load Washer%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/4106487/pexels-photo-4106487.jpeg' 
WHERE name LIKE '%Philips Air Fryer HD9252%';

-- FASHION
UPDATE product SET image_url = 'https://images.pexels.com/photos/994523/pexels-photo-994523.jpeg' 
WHERE name LIKE '%Zara Floral Print Midi Dress%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/994523/pexels-photo-994523.jpeg' 
WHERE name LIKE '%Global Desi Maxi Dress%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/19090/pexels-photo.jpg' 
WHERE name LIKE '%Woodland Leather Boots%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/19090/pexels-photo.jpg' 
WHERE name LIKE '%Bata Formal Oxford Shoes%';

-- SPORTS
UPDATE product SET image_url = 'https://images.pexels.com/photos/100582/pexels-photo-100582.jpeg' 
WHERE name LIKE '%Decathlon MTB ST 120%';

-- TOYS
UPDATE product SET image_url = 'https://images.pexels.com/photos/163036/matchbox-car-toy-car-model-163036.jpeg' 
WHERE name LIKE '%Hot Wheels Garage%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/3662667/pexels-photo-3662667.jpeg' 
WHERE name LIKE '%Barbie Dreamhouse%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/3662667/pexels-photo-3662667.jpeg' 
WHERE name LIKE '%Nerf Elite 2.0 Blaster%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/278918/pexels-photo-278918.jpeg' 
WHERE name LIKE '%Monopoly Classic%';

UPDATE product SET image_url = 'https://images.pexels.com/photos/278918/pexels-photo-278918.jpeg' 
WHERE name LIKE '%Scrabble Original%';

-- HOME
UPDATE product SET image_url = 'https://images.pexels.com/photos/164595/pexels-photo-164595.jpeg' 
WHERE name LIKE '%Home Centre Bedsheet King%';

-- Verify updates
SELECT id, name, price, image_url FROM product 
WHERE name IN (
    'Apple iPhone 14 128GB', 'HP Spectre x360 2-in-1', 'Samsung Galaxy Book3 Pro 360',
    'Bose QuietComfort Ultra', 'Sony WH-1000XM5 Wireless', 'Samsung HW-Q990C Soundbar',
    'JBL Flip 6 Bluetooth Speaker', 'Sonos Era 300 Speaker', 'Sony Alpha 7 IV Mirrorless',
    'Fujifilm X-T5 Mirrorless', 'DJI Mini 4 Pro Drone', 'LG 7kg Front Load Washer',
    'Philips Air Fryer HD9252', 'Zara Floral Print Midi Dress', 'Global Desi Maxi Dress',
    'Woodland Leather Boots', 'Bata Formal Oxford Shoes', 'Decathlon MTB ST 120',
    'Hot Wheels Garage', 'Barbie Dreamhouse 2024', 'Nerf Elite 2.0 Blaster',
    'Monopoly Classic', 'Scrabble Original', 'Home Centre Bedsheet King'
)
ORDER BY id;
