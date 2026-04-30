-- Seed data for 100 products across multiple categories
-- Run this after the databases are created

USE product_catalog_service;

-- Insert Categories
INSERT INTO category (name, description, created_at, last_updated_at, state) VALUES
('Phones & Mobile', 'Smartphones and mobile accessories', NOW(), NOW(), 0),
('Laptops & Computers', 'Laptops, desktops, and computer accessories', NOW(), NOW(), 0),
('TVs & Audio', 'Televisions, sound systems, and audio equipment', NOW(), NOW(), 0),
('Cameras & Accessories', 'Digital cameras, lenses, and photography gear', NOW(), NOW(), 0),
('Home Appliances', 'Home and kitchen appliances', NOW(), NOW(), 0),
('Men\'s Fashion', 'Men\'s clothing and accessories', NOW(), NOW(), 0),
('Women\'s Fashion', 'Women\'s clothing and accessories', NOW(), NOW(), 0),
('Footwear', 'Shoes, sneakers, and footwear', NOW(), NOW(), 0),
('Books', 'Physical books and publications', NOW(), NOW(), 0),
('Sports & Outdoors', 'Sports equipment and outdoor gear', NOW(), NOW(), 0),
('Beauty & Personal Care', 'Beauty products and personal care items', NOW(), NOW(), 0),
('Toys & Games', 'Toys, games, and entertainment', NOW(), NOW(), 0),
('Home & Garden', 'Home decor, furniture, and garden supplies', NOW(), NOW(), 0);

-- Insert Products
-- Phones & Mobile (Category ID: 1)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Apple iPhone 15 Pro Max 256GB', 'Latest iPhone with titanium design, A17 Pro chip, and advanced camera system', 149900.00, 'https://store.storeimages.cdn-apple.com/iphone-15-pro.png', 50, 1, NOW(), NOW(), 0),
('Samsung Galaxy S24 Ultra 512GB', 'Flagship Android phone with S Pen, AI features, and 200MP camera', 134999.00, 'https://images.samsung.com/is/image/samsung/p6pim/in/2401/gallery/in-galaxy-s24-s928-sm-s928bzshins-539573252?$730_584_PNG$', 45, 1, NOW(), NOW(), 0),
('Google Pixel 8 Pro 128GB', 'Google\'s premium phone with best-in-class AI photography', 106999.00, 'https://lh3.googleusercontent.com/gdNgW8c9wH7ZpPldhzzu0jcTzqcLPbINr4myc0sC8MYa0VnqEdzWHalXA8ZzOC8Bjh76-eyMOHAZ3xIuhGkd6vVmplLADrzMW3c=p', 35, 1, NOW(), NOW(), 0),
('OnePlus 12 256GB', 'Performance flagship with Hasselblad cameras and 100W charging', 64999.00, 'https://image01.oneplus.net/media/2024/01/09/4a76c75696c9bd6388fe9bdec91f0b85.png?x-amz-process=image/format,webp/quality,Q_80', 40, 1, NOW(), NOW(), 0),
('Xiaomi Redmi Note 13 Pro+ 256GB', 'Mid-range killer with 200MP camera and 120W fast charging', 31999.00, 'https://i02.appmifile.com/313_operator_sg/13/01/2024/8a6e86bd01c18cb9e429c90d95f74c42.png', 60, 1, NOW(), NOW(), 0),
('Apple iPhone 14 128GB', 'Previous generation iPhone with excellent performance', 69900.00, 'https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-14-finish-select-202209-6-1inch-starlight?wid=5120&hei=2880&fmt=webp&qlt=70&.v=NjB3M1ZveDhpTGkwSDdWVWNzQVRVVEJPMXBLNDFSNXhKZG1wZWZrNDB1VUE4UjZRUGRUT0NVNnpTVkNhMVhHQUo1TVVuYlhvcGZSVVdxV09ZYlMzL1dQdDJHaGg3Z0ZjeEJ0dVZEZ3RZeXZmQmpVSlNtZWxFWnU4dEpRRG5Ea2hzdkF6ZFlWYnltTEZEYXF1ZzJKSkF3NlFJaVBlNFpSbTlRYU9RYjhMcUdWOU5oSFNGdmR6cU5RMVIzbjNQYnJkQS94cmR6NDl4aXptMjdwZDQ9&traceId=1', 55, 1, NOW(), NOW(), 0),
('Samsung Galaxy Z Fold5 512GB', 'Foldable phone with large screen and multitasking capabilities', 154999.00, 'https://images.samsung.com/is/image/samsung/p6pim/in/galaxy-z-fold5/gallery/in-galaxy-z-fold5-f946-sm-f946bzsdins-539573200?$624_624_PNG$', 25, 1, NOW(), NOW(), 0),
('Motorola Edge 40 Neo 256GB', 'Stylish mid-range with curved display and wireless charging', 23999.00, 'https://motorolain.vtexassets.com/arquivos/ids/159590/motorola-edge-40-neo-caneel-bay-pdp-ecom-hero.png?v=638305427801900000', 50, 1, NOW(), NOW(), 0),
('Nothing Phone 2 128GB', 'Unique transparent design with Glyph Interface', 44999.00, 'https://in.nothing.tech/cdn/shop/files/Phone-2-Dark-Grey-Front-Angled_300x.png?v=1698753367', 30, 1, NOW(), NOW(), 0),
('Realme GT 5 Pro 256GB', 'Performance flagship with Snapdragon 8 Gen 3', 45999.00, 'https://image01.realme.net/general/20231211/1702289986134.png.webp', 45, 1, NOW(), NOW(), 0);

-- Laptops & Computers (Category ID: 2)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Apple MacBook Pro 14-inch M3', 'Professional laptop with M3 chip and Liquid Retina XDR display', 169900.00, 'https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/macbook-pro-14-og-202310?wid=1200&hei=630&fmt=jpeg&qlt=95&.v=1696979343700', 20, 2, NOW(), NOW(), 0),
('Dell XPS 15 OLED Laptop', 'Premium laptop with 3.5K OLED display and RTX 4050', 219990.00, 'https://i.dell.com/is/image/DellContent/content/dam/ss2/product-images/dell-client-products/notebooks/xps-notebooks/xps-15-9530/media-gallery/og-xps-15-9530-gallery-800x620.png?fmt=png-alpha&wid=800&hei=620', 15, 2, NOW(), NOW(), 0),
('HP Spectre x360 2-in-1 Laptop', 'Convertible laptop with OLED touch display', 149999.00, 'https://ssl-product-images.www8-hp.com/digmedialib/prodimg/lowres/c08377018.png', 25, 2, NOW(), NOW(), 0),
('Lenovo ThinkPad X1 Carbon Gen 11', 'Business ultrabook with Intel vPro and military-grade durability', 185000.00, 'https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-carbon-gen-11-hero.png?context=bWFzdGVyfHJvb3R8MTEwMjQ0fGltYWdlL3BuZ3xoMjIvaDQ4LzE2NjE5MTM3MjA1NzI2LnBuZ3w5NjZlMjYyMGMxNDMwMjNkZmJmNGE0MjA3Y2EzM2IwOWViYzhmM2Y3YzEwMGE0ZTAyZmMyZTg2MjIwNjFjNmQ4', 18, 2, NOW(), NOW(), 0),
('ASUS ROG Zephyrus G14 Gaming Laptop', 'Compact gaming powerhouse with RTX 4070', 154990.00, 'https://dlcdnwebimgs.asus.com/gain/7E95FB2B-0F23-4A0D-8726-7B563F4759AC/w1000/h732', 22, 2, NOW(), NOW(), 0),
('Microsoft Surface Laptop Studio 2', 'Versatile laptop with unique hinge design and RTX 4060', 198999.00, 'https://cdn-dynmedia-1.microsoft.com/is/image/microsoftcorp/SLaptopStudio2-Platinum-Angles-1_RTIu1k?fmt=png-alpha&scl=1', 12, 2, NOW(), NOW(), 0),
('Acer Swift 5 Ultrabook', 'Lightweight laptop with Intel Evo certification', 92999.00, 'https://images.acer.com/is/image/acer/swift-5-sf514-56t-fingerprint-backlit-on-wallpaper-win11-pure-silver-01-1?$product-list$', 30, 2, NOW(), NOW(), 0),
('Apple MacBook Air 13-inch M2', 'Thin and light laptop with M2 chip', 114900.00, 'https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/macbook-air-og-202206?wid=1200&hei=630&fmt=jpeg&qlt=95&.v=1653089919389', 35, 2, NOW(), NOW(), 0),
('Razer Blade 15 Gaming Laptop', 'Premium gaming laptop with RTX 4070 and 240Hz display', 249999.00, 'https://assets2.razerzone.com/images/pnx.assets/f0c9b7c61c7c9d448240d6a96369a849/razer-blade-15-2023-og.jpg', 10, 2, NOW(), NOW(), 0),
('Samsung Galaxy Book3 Pro 360', '2-in-1 laptop with S Pen and AMOLED display', 134990.00, 'https://images.samsung.com/is/image/samsung/p6pim/in/galaxy-book3-pro-360/gallery/in-galaxy-book3-pro-360-16-inch-np960qfg-kc2in-539573247?$624_624_PNG$', 20, 2, NOW(), NOW(), 0);

-- TVs & Audio (Category ID: 3)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Samsung 65" Neo QLED 4K Smart TV', 'Premium TV with Quantum Matrix Technology and Dolby Atmos', 249990.00, 'https://images.samsung.com/is/image/samsung/p6pim/in/qn65qn90cagxxl/gallery/in-neo-qled-qn90c-qn65qn90cagxxl-539573252?$624_624_PNG$', 15, 3, NOW(), NOW(), 0),
('LG 55" OLED C3 Series TV', 'OLED TV with α9 Gen6 AI Processor and webOS', 149990.00, 'https://www.lg.com/in/images/tv/md07554746/gallery/OLED55C3PCA-D-01.jpg', 20, 3, NOW(), NOW(), 0),
('Sony 75" Bravia XR A80L OLED TV', 'Cognitive Processor XR with OLED panel', 299990.00, 'https://www.sony.co.in/image/5d02e5c32e6f24f7c3d0b6e0c5c6f0a3?fmt=pjpeg&wid=330&bgcolor=FFFFFF', 10, 3, NOW(), NOW(), 0),
('TCL 50" 4K UHD Smart TV', 'Affordable 4K TV with Google TV and HDR10+', 28990.00, 'https://www.tcl.com/content/dam/tcl/product/tv/p-series/p635/p635-right-global.png', 40, 3, NOW(), NOW(), 0),
('Bose QuietComfort Ultra Headphones', 'Premium noise cancelling headphones with spatial audio', 35900.00, 'https://assets.bose.com/content/dam/Bose_DAM/Web/primary_images/headphones/qc_ultra_headphones/QC_Ultra_Headphones_Black_1_1.png/jcr:content/renditions/cq5dam.web.600.600.png', 45, 3, NOW(), NOW(), 0),
('Sony WH-1000XM5 Wireless Headphones', 'Industry-leading noise cancellation with 30hr battery', 29990.00, 'https://www.sony.co.in/image/5d02e5c32e6f24f7c3d0b6e0c5c6f0a3?fmt=pjpeg&wid=330&bgcolor=FFFFFF', 50, 3, NOW(), NOW(), 0),
('Apple AirPods Pro 2nd Gen', 'Active noise cancellation with H2 chip', 24900.00, 'https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/airpods-pro-2-og-202209?wid=1200&hei=630&fmt=jpeg&qlt=95&.v=1661024559202', 60, 3, NOW(), NOW(), 0),
('Samsung HW-Q990C Soundbar', '11.1.4 channel soundbar with Dolby Atmos', 94990.00, 'https://images.samsung.com/is/image/samsung/p6pim/in/hw-q990c-xl/gallery/in-q-series-soundbar-hw-q990c-hw-q990c-xl-539573252?$624_624_PNG$', 18, 3, NOW(), NOW(), 0),
('JBL Flip 6 Portable Bluetooth Speaker', 'Waterproof speaker with bold JBL sound', 11999.00, 'https://www.jbl.com/dw/image/v2/BFND_PRD/on/demandware.static/-/Sites-masterCatalog_Harman/default/dw2c8e3f9f/JBL_Flip_6_Hero_1605x1605.png?sw=800&sh=800', 55, 3, NOW(), NOW(), 0),
('Sonos Era 300 Smart Speaker', 'Spatial audio speaker with Dolby Atmos', 44999.00, 'https://www.sonos.com/_next/image?url=https%3A%2F%2Fassets.sonos.com%2Fcdn%2Ff%2F8554553%2F2024%2F03%2F12%2F1900x1900_Era300_Black_Hero_Angle_v1.jpg&w=3840&q=75', 25, 3, NOW(), NOW(), 0);

-- Cameras & Accessories (Category ID: 4)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Sony Alpha 7 IV Mirrorless Camera', '33MP full-frame hybrid camera for photo and video', 249990.00, 'https://www.sony.co.in/image/5d02e5c32e6f24f7c3d0b6e0c5c6f0a3?fmt=pjpeg&wid=330&bgcolor=FFFFFF', 12, 4, NOW(), NOW(), 0),
('Canon EOS R6 Mark II', 'Full-frame mirrorless with 40fps burst shooting', 215995.00, 'https://in.canon/media/image/2022/11/08/d5a7f6a8-7b6c-4e3f-9a3b-6d3f8a1c2b9c_eos-r6-mark-ii-ef-24-105mm-f4l-is-usm.png', 15, 4, NOW(), NOW(), 0),
('Nikon Z8 Mirrorless Camera', '45.7MP full-frame with 8K video recording', 319995.00, 'https://www.nikon.co.in/media/catalog/product/z/8/nikon_z8_body_only.png', 8, 4, NOW(), NOW(), 0),
('Fujifilm X-T5 Mirrorless Camera', '40MP APS-C camera with classic dial operation', 169999.00, 'https://www.fujifilm.in/products/cameras/x-t5/img/index/main_visual_01.jpg', 20, 4, NOW(), NOW(), 0),
('DJI Mini 4 Pro Drone', 'Lightweight drone with 4K/60fps HDR video', 112999.00, 'https://www-cdn.djiits.com/dps/3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d.png', 18, 4, NOW(), NOW(), 0),
('GoPro Hero 12 Black Action Camera', 'Ultimate action camera with 5.3K video', 44999.00, 'https://gopro.com/content/dam/gopro/product-assets/hero12-black/hero12-black-master-image.png', 35, 4, NOW(), NOW(), 0),
('Insta360 X3 360 Camera', '5.7K 360-degree action camera', 44999.00, 'https://res.insta360.com/static/infr_sdk/194a0a6c7a3b2d1e5f4c8b9a0e7d6f5c.png', 30, 4, NOW(), NOW(), 0),
('Canon RF 24-70mm f/2.8L Lens', 'Professional standard zoom lens', 209995.00, 'https://in.canon/media/image/2022/11/08/d5a7f6a8-7b6c-4e3f-9a3b-6d3f8a1c2b9c_rf24-70mm-f28l-is-usm.png', 10, 4, NOW(), NOW(), 0);

-- Home Appliances (Category ID: 5)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Dyson V15 Detect Vacuum Cleaner', 'Laser detect technology with powerful suction', 62900.00, 'https://www.dyson.in/media/images/products/370x370/44896.jpg', 20, 5, NOW(), NOW(), 0),
('iRobot Roomba j9+ Robot Vacuum', 'Self-emptying robot vacuum with smart mapping', 89900.00, 'https://www.irobot.in/media/catalog/product/r/o/roomba-j9-plus-1_1.png', 15, 5, NOW(), NOW(), 0),
('LG 7kg Front Load Washing Machine', 'AI Direct Drive with steam technology', 35990.00, 'https://www.lg.com/in/images/washing-machines/md07547766/gallery/FHV1207ZWP-Washing-Machines-Front-View-DZ-01.jpg', 25, 5, NOW(), NOW(), 0),
('Samsung 28L Convection Microwave', 'Slim fry technology for healthy cooking', 13490.00, 'https://images.samsung.com/is/image/samsung/p6pim/in/mc28a5013ak-tl/gallery/in-convection-microwave-28l-mc28a5013ak-tl-539573252?$624_624_PNG$', 30, 5, NOW(), NOW(), 0),
('Whirlpool 340L Frost Free Refrigerator', 'IntelliSense inverter technology', 29990.00, 'https://www.whirlpoolindia.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/i/f/if-inv-cnv-355-illusia-steel-3s-1.jpg', 20, 5, NOW(), NOW(), 0),
('Philips Air Fryer HD9252/91', 'Digital air fryer with Rapid Air Technology', 8999.00, 'https://www.philips.co.in/c-dam/b2c/category-pages/Household/Kitchen/Cooking/Airfryer/HD9252-91/HD9252-91-01.png', 40, 5, NOW(), NOW(), 0),
('Nespresso Vertuo Plus Coffee Maker', 'Centrifusion technology for rich coffee', 14999.00, 'https://www.nespresso.com/ecom/medias/sys_master/public/15070432649246/VertuoPlus-Black-1.png', 35, 5, NOW(), NOW(), 0),
('Bosch Series 4 Dishwasher', '12 place settings with EcoSilence Drive', 35990.00, 'https://www.bosch-home.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/s/m/sms4hdi52e.png', 18, 5, NOW(), NOW(), 0),
('IFB 25L Grill Microwave Oven', 'Multi-stage cooking with 101 auto cook menus', 13490.00, 'https://www.ifbappliances.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/2/5/25bgrc1_1.png', 28, 5, NOW(), NOW(), 0),
('Prestige 3L Pressure Cooker', 'Popular aluminum pressure cooker', 1499.00, 'https://www.prestigesmartkitchen.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/p/r/prestige-3l-popular-pressure-cooker.png', 50, 5, NOW(), NOW(), 0),
('Kent Grand Plus Water Purifier', 'RO+UV+UF with TDS controller', 17999.00, 'https://www.kent.co.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/k/e/kent-grand-plus-11099.png', 22, 5, NOW(), NOW(), 0),
('Havells 16L Storage Water Heater', 'Safe shock-proof body with 5-star rating', 8999.00, 'https://www.havells.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/f/a/fasto-16l.png', 35, 5, NOW(), NOW(), 0);

-- Men\'s Fashion (Category ID: 6)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Levi\'s 511 Slim Fit Jeans - Dark Indigo', 'Classic slim fit jeans with stretch comfort', 2999.00, 'https://www.levi.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/l/e/levis-mens-511-slim-fit-jeans-dark-indigo-1.jpg', 60, 6, NOW(), NOW(), 0),
('Nike Dri-FIT Running T-Shirt - Black', 'Sweat-wicking fabric for intense workouts', 1995.00, 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d/dri-fit-running-t-shirt-black.png', 80, 6, NOW(), NOW(), 0),
('Adidas Originals Trefoil Hoodie - Grey', 'Iconic hoodie with embroidered logo', 3999.00, 'https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d_9366/Trefoil_Hoodie_Grey.jpg', 45, 6, NOW(), NOW(), 0),
('Puma Essentials Logo Tee - White', 'Cotton tee with classic Puma branding', 1299.00, 'https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/586759/01/fnd/IND/fmt/png/Essentials-Logo-Men\'s-Tee', 100, 6, NOW(), NOW(), 0),
('Van Heusen Formal Shirt - Light Blue', '100% cotton wrinkle-free formal shirt', 2499.00, 'https://www.vanheusen.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/v/h/vh-men-formal-shirt-light-blue-1.jpg', 55, 6, NOW(), NOW(), 0),
('Peter England Cotton Chinos - Beige', 'Comfort fit chinos for everyday wear', 2199.00, 'https://www.peterengland.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/p/e/pe-men-chinos-beige-1.jpg', 70, 6, NOW(), NOW(), 0),
('Woodland Leather Jacket - Brown', 'Genuine leather jacket for rugged style', 8999.00, 'https://www.woodlandworldwide.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/w/o/woodland-men-leather-jacket-brown-1.jpg', 25, 6, NOW(), NOW(), 0),
('Raymond Woolen Blazer - Navy Blue', 'Premium wool blazer for formal occasions', 7499.00, 'https://www.raymond.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/r/a/raymond-men-blazer-navy-blue-1.jpg', 30, 6, NOW(), NOW(), 0);

-- Women\'s Fashion (Category ID: 7)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Zara Floral Print Midi Dress', 'Elegant floral dress for summer occasions', 2990.00, 'https://static.zara.net/photos///2024/V/0/1/p/2567/072/330/2/w/1126/2567072330_1_1_1.jpg?ts=1706543215000', 40, 7, NOW(), NOW(), 0),
('H&M Oversized Linen Shirt - White', 'Breathable linen shirt with relaxed fit', 1999.00, 'https://lp2.hm.com/hmgoepprod?set=quality%5B79%5D%2Csource%5B%2F3b%2F5e%2F3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d3b5e3f8c.jpg%5D%2Corigin%5Bdam%5D%2Ccategory%5B%5D%2Ctype%5BDESCRIPTIVESTILLLIFE%5D%2Cres%5Bm%5D%2Chmver%5B2%5D&call=url[file:/product/main]', 55, 7, NOW(), NOW(), 0),
('Forever 21 High-Waisted Skinny Jeans', 'Figure-flattering jeans with stretch', 1899.00, 'https://www.forever21.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/f/2/f21-women-jeans-skinny-1.jpg', 65, 7, NOW(), NOW(), 0),
('Mango Wool Blend Coat - Camel', 'Sophisticated coat for winter elegance', 5990.00, 'https://static.mango.com/assets/rcs/pics/static/Toy/f800015491704.jpg?imwidth=2048&imdensity=1&ts=1706543215000', 20, 7, NOW(), NOW(), 0),
('Biba Anarkali Kurta - Maroon', 'Traditional ethnic wear with embroidery', 3499.00, 'https://www.biba.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/b/i/biba-women-anarkali-kurta-maroon-1.jpg', 50, 7, NOW(), NOW(), 0),
('W Cotton Kurti with Palazzo - Pink', 'Comfortable ethnic set for daily wear', 1999.00, 'https://www.wforwoman.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/w/w-ethnic-set-pink-1.jpg', 60, 7, NOW(), NOW(), 0),
('Global Desi Printed Maxi Dress', 'Bohemian style dress with vibrant prints', 2799.00, 'https://www.globaldesi.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/g/d/gd-women-maxi-dress-1.jpg', 45, 7, NOW(), NOW(), 0),
('ONLY Denim Jacket - Light Wash', 'Versatile jacket for casual styling', 2499.00, 'https://www.only.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/o/n/only-women-denim-jacket-light-wash-1.jpg', 35, 7, NOW(), NOW(), 0);

-- Footwear (Category ID: 8)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Nike Air Jordan 1 Retro High', 'Iconic basketball shoe in classic colorway', 14995.00, 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d/air-jordan-1-retro-high-og.png', 40, 8, NOW(), NOW(), 0),
('Adidas Ultraboost Light Running Shoes', 'Lightweight performance running shoes', 15999.00, 'https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d_9366/Ultraboost_Light_Running_Shoes.jpg', 55, 8, NOW(), NOW(), 0),
('Puma RS-X Bold Sneakers', 'Chunky retro-style sneakers', 8999.00, 'https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/374220/01/fnd/IND/fmt/png/RS-X-Bold-Sneakers', 70, 8, NOW(), NOW(), 0),
('Crocs Classic Clogs - Black', 'Iconic comfortable footwear', 3495.00, 'https://www.crocs.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/c/r/crocs-classic-clog-black.jpg', 100, 8, NOW(), NOW(), 0),
('Woodland Leather Boots - Tan', 'Rugged outdoor boots with commando sole', 7495.00, 'https://www.woodlandworldwide.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/w/o/woodland-men-leather-boots-tan.jpg', 45, 8, NOW(), NOW(), 0),
('Bata Formal Oxford Shoes - Black', 'Classic formal shoes for office wear', 2999.00, 'https://www.bata.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/b/a/bata-men-oxford-shoes-black.jpg', 60, 8, NOW(), NOW(), 0);

-- Books (Category ID: 9)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Atomic Habits by James Clear', 'Build good habits and break bad ones', 499.00, 'https://m.media-amazon.com/images/I/51-nXsSRfZL._SX328_BO1,204,203,200_.jpg', 80, 9, NOW(), NOW(), 0),
('The Psychology of Money by Morgan Housel', 'Timeless lessons on wealth and happiness', 399.00, 'https://m.media-amazon.com/images/I/71j0FLAmsDL._SY466_.jpg', 75, 9, NOW(), NOW(), 0),
('Rich Dad Poor Dad by Robert Kiyosaki', 'Financial education classic', 299.00, 'https://m.media-amazon.com/images/I/81bsw6fnUiL._SY466_.jpg', 100, 9, NOW(), NOW(), 0),
('Sapiens by Yuval Noah Harari', 'Brief history of humankind', 599.00, 'https://m.media-amazon.com/images/I/713jIoMO3UL._SY466_.jpg', 60, 9, NOW(), NOW(), 0),
('Think and Grow Rich by Napoleon Hill', 'Classic success philosophy', 249.00, 'https://m.media-amazon.com/images/I/71Ad.+Xk4WL._SY466_.jpg', 90, 9, NOW(), NOW(), 0),
('The Alchemist by Paulo Coelho', 'Inspirational tale about following dreams', 350.00, 'https://m.media-amazon.com/images/I/51Z0nLAfLmL._SX332_BO1,204,203,200_.jpg', 85, 9, NOW(), NOW(), 0);

-- Sports & Outdoors (Category ID: 10)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Yonex Astrox 100ZZ Badminton Racket', 'Professional grade badminton racket', 18999.00, 'https://www.yonex.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/a/s/astrox-100zz-kurenai.png', 25, 10, NOW(), NOW(), 0),
('SG Test Cricket Bat - English Willow', 'Premium quality cricket bat', 7499.00, 'https://www.sgsports.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/s/g/sg-test-cricket-bat.png', 30, 10, NOW(), NOW(), 0),
('Nike Pitch Training Football', 'Durable football for practice', 1499.00, 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d/pitch-training-football.png', 50, 10, NOW(), NOW(), 0),
('Decathlon MTB ST 120 Mountain Bike', 'Affordable mountain bike for beginners', 14999.00, 'https://contents.mediadecathlon.com/p1993420/k$06e0f3f8c7d6e4a1b9c8d5f7e6a3b4c5d/sq/MTB+ST+120+27.5.png?f=250x250', 20, 10, NOW(), NOW(), 0),
('Fitbit Charge 6 Fitness Tracker', 'Advanced fitness and health tracking', 14999.00, 'https://www.fitbit.com/content/dam/fitbit/global/pdp/charge-6/hero-static/Charge6-Black-Holiday.png', 40, 10, NOW(), NOW(), 0),
('Adidas Yoga Mat - 6mm', 'Comfortable cushioned yoga mat', 1999.00, 'https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/3b5e3f8c7d6e4a1b9c8d5f7e6a3b4c5d_9366/Yoga_Mat_6mm.jpg', 60, 10, NOW(), NOW(), 0);

-- Beauty & Personal Care (Category ID: 11)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('Lakme Absolute Skin Natural Mousse Foundation', 'Lightweight foundation with SPF', 875.00, 'https://www.lakmeindia.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/l/a/lakme-mousse-foundation.png', 70, 11, NOW(), NOW(), 0),
('Maybelline New York Colossal Kajal', 'Deep black smudge-proof kajal', 299.00, 'https://www.maybelline.co.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/m/a/maybelline-colossal-kajal.png', 100, 11, NOW(), NOW(), 0),
('Forest Essentials Soundarya Beauty Cream', 'Luxury Ayurvedic face cream', 3650.00, 'https://www.forestessentialsindia.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/s/o/soundarya-beauty-cream.png', 35, 11, NOW(), NOW(), 0),
('The Body Shop British Rose Body Butter', 'Hydrating body butter with rose extract', 1095.00, 'https://www.thebodyshop.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/b/r/british-rose-body-butter.png', 50, 11, NOW(), NOW(), 0),
('Philips Series 9000 Prestige Shaver', 'Advanced electric shaver with AI', 24995.00, 'https://www.philips.co.in/c-dam/b2c/category-pages/Household/Male-Grooming/Shaver/series-9000-prestige.png', 25, 11, NOW(), NOW(), 0),
('Braun Silk-épil 9 Flex Epilator', 'Wet and dry epilator with flexible head', 15999.00, 'https://www.braunhousehold.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/s/i/silk-epil-9-flex.png', 30, 11, NOW(), NOW(), 0);

-- Toys & Games (Category ID: 12)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('LEGO Technic Ferrari Daytona SP3', 'Collectible Ferrari model with 3778 pieces', 38999.00, 'https://www.lego.com/cdn/cs/set/assets/blt5d6e8f8b7a3c2d1e/42143_alt1.png', 15, 12, NOW(), NOW(), 0),
('Hot Wheels Ultimate Garage Playset', 'Multi-level garage with racing tracks', 7999.00, 'https://shop.mattel.com/cdn/shop/products/fth77_01_2000x.png?v=1706543215', 25, 12, NOW(), NOW(), 0),
('Barbie Dreamhouse Playset 2024', 'Interactive dollhouse with lights and sounds', 29999.00, 'https://shop.mattel.com/cdn/shop/products/barbie-dreamhouse-2024.png?v=1706543215', 20, 12, NOW(), NOW(), 0),
('Nerf Elite 2.0 Blaster', 'Motorized dart blaster with 20 darts', 2499.00, 'https://m.media-amazon.com/images/I/71aB4C1nR0L._AC_SL1500_.jpg', 40, 12, NOW(), NOW(), 0),
('Hasbro Gaming Monopoly Classic', 'Classic property trading board game', 1499.00, 'https://shop.hasbro.com/cdn/shop/products/monopoly-classic.png?v=1706543215', 60, 12, NOW(), NOW(), 0),
('Scrabble Original Board Game', 'Word-building board game for all ages', 1299.00, 'https://shop.mattel.com/cdn/shop/products/scrabble-original.png?v=1706543215', 55, 12, NOW(), NOW(), 0);

-- Home & Garden (Category ID: 13)
INSERT INTO product (name, description, price, image_url, inventory_quantity, category_id, created_at, last_updated_at, state) VALUES
('IKEA POÄNG Armchair - Birch/Brown', 'Classic bentwood armchair with cushion', 7999.00, 'https://www.ikea.com/in/en/images/products/poaeng-armchair-birch-veneer-brown__0559474_pe661775_s5.jpg?f=xl', 30, 13, NOW(), NOW(), 0),
('Home Centre Cotton Bedsheet Set - King', 'Premium cotton bedsheet with pillow covers', 2499.00, 'https://www.homecentre.in/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/h/c/hc-bedsheet-king-1.jpg', 45, 13, NOW(), NOW(), 0),
('Philips Hue White & Color Ambiance Starter Kit', 'Smart lighting with 16 million colors', 16999.00, 'https://www.philips-hue.com/cdn/shop/products/hue-white-color-ambiance-starter-kit.png?v=1706543215', 25, 13, NOW(), NOW(), 0),
('Dyson Pure Cool Tower Air Purifier', 'Air purifier and fan in one', 45900.00, 'https://www.dyson.in/media/images/products/370x370/44896.jpg', 15, 13, NOW(), NOW(), 0);
