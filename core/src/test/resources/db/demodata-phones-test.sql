insert into colors
values (1000, 'Black'),
       (1001, 'White'),
       (1002, 'Yellow');

insert into phones
values ('1000', 'ARCHOS3', 'ARCHOS 101 Helium 4G', 500, 10.1, 482, 276.0, 167.0, 12.6, null, 'Tablet', 'Android (4.0)',
        '1280 x  800', 149, null, null, 1.3, null, 8.0, null, null, null, '2.1, EDR', 'GPS',
        'manufacturer/ARCHOS/ARCHOS 101 G9.jpg',
        'The ARCHOS 101 G9 is a 10.1'''' tablet, equipped with Google''s open source OS. It offers a multi-core ARM CORTEX A9 processor at 1GHz, 8 or 16GB internal memory, microSD card slot, GPS, Wi-Fi, Bluetooth 2.1, and more.'),
       ('1001', 'ARCHOS7', 'ARCHOS 101 Saphir', 360, 8.5, 482, 250.0, 174.0, 10.0, null, 'Tablet', 'Android (4.4)',
        '1280 x  800', 149, 'IPS LCD', 5.0, 0.3, 1.0, 8.0, 6500, null, null, 'Yes', 'GPS, A-GPS',
        'manufacturer/ARCHOS/ARCHOS 101 Helium 4G.jpg',
        'The ARCHOS 101 Helium 4G is a 10.1" tablet, running Android 4.4. Its specs include 1.5 GHz quad core MediaTek processor, 1 GB RAM, 8 GB storage space, further expandable via microSD card slot, 4G LTE and 2 cameras - 5 MP on the back, and 0.3 MP on the front.'),
       ('1002', 'ARCHOS4', 'ARCHOS 101 Internet Tablet', 200, 12.6, 482, null, null, null, null, 'Tablet',
        'Android (2.2)', '1024 x  600', 118, 'TFT', null, 0.3, null, 8.0, null, null, null, '2.1, EDR', null,
        'manufacturer/ARCHOS/ARCHOS 101 Internet Tablet.jpg',
        'Archos 101 Internet Tablet is 10.1" Android running slate with Wi-Fi connectivity. It has 1GHz processor, 0.3MP front camera, HDMI port, 8GB internal memory and supports up to 32GB microSD memory cards.'),
       ('1003', 'ARCHOS8', 'ARCHOS 101 G9', 249, 10.3, 482, 240.0, 172.0, 10.0, null, 'Tablet', 'Android (4.4.4)',
        '1200 x  1920', 224, 'IPS LCD', 5.0, 2.0, 2.0, 16.0, 7000, null, null, 'Yes', 'GPS, A-GPS',
        'manufacturer/ARCHOS/ARCHOS 101 Oxygen.jpg',
        'The Archos 101 Oxygen is a tablet that offers a 10.1-inch 1080p display, 1.5 GB of RAM, 16 GB of internal memory, and a quad-core processor.'),
       ('1004', 'ARCHOS', 'ARCHOS 101 Oxygen', 270, 10.2, 482, 265.4, 181.0, 13.4, null, 'Tablet', 'Android (7.0)',
        '1280 x  800', 149, 'IPS LCD', 5.0, 2.0, 1.0, 16.0, 6000, null, null, 'Yes', 'GPS, A-GPS',
        'manufacturer/ARCHOS/ARCHOS 101 Saphir.jpg',
        'The front of the Saphir 101 is a 10.1-inch IPS display, and while the resolution of 1280 x 800 isn''t the sharpest around, it should be perfectly serviceable for browsing the web, typing out Word documents, watching videos, etc. Android 7.0 Nougat is powering the 101 right out of the box, and the MediaTek MT8163 CPU and 1GB of RAM should provide enough power as long as you aren''t doing anything too intensive on the machine.');

insert into phone2color
values (1000, 1000),
       (1001, 1000),
       (1001, 1001),
       (1002, 1000),
       (1002, 1001),
       (1002, 1002),
       (1004, 1000),
       (1004, 1002);

insert into stocks
values (1001, 11, 0),
       (1002, 12, 1),
       (1003, 13, 2),
       (1004, 14, 3);

insert into orders
values (1, 'l123', 200, 5, 205, 'nnn', 'kkk', 'Minsk', '1234567', null, 'NEW');

insert into phone2order
values (1, 1001, 2),
       (1, 1002, 3);