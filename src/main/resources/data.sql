
INSERT INTO app_user (name, email, password, phone, address, is_notification) VALUES
('Chemsou Br', 'chemseddine.berkane@gmail.com', '$2a$10$9h3/gccUBYEexkqWsV/3qOuCfNPX5PWQ92kq0GvtcWQLgpATgjXtO', '0123456789', '38 rue de Clermon-Ferrand, France', true),
('Sama Br', 'chamseddineb07@gmail.com', '$2a$10$9h3/gccUBYEexkqWsV/3qOuCfNPX5PWQ92kq0GvtcWQLgpATgjXtO', '0123456789', '38 rue de Clermon-Ferrand, France', true);;

INSERT INTO category (name) VALUES
('Vêtements'),('Électronique'),('Décoration'),('Livres'),('Automobile'),('Autres');

INSERT INTO item (
    title,
    description,
    location,
    key_words,
    picture,
    status,
    delivery,
    created_at,
    updated_at,
    category_id,
    user_id,
    disabled
) VALUES (
    'Pantalon homme', 
    'Pantalon en coton, taille L avec une bonne qualité. Une couleur bleue avec quelques détails en dessous.', 
    'Lyon, France', 
    'pantalon, vetement, homme', 
    'https://fitostic.com/wp-content/uploads/2021/04/lepantalon-burgundy-chinos-trousers-pantalon-bordeaux-homme.jpg', 
    'BON', 
    'LIVRAISON', 
    NOW(), 
    NOW(), 
    1,           -- category_id (Assurez-vous que la catégorie avec l'ID 2 existe)
    1,           -- user_id (Assurez-vous que l'utilisateur avec l'ID 1 existe),
    false
);


INSERT INTO item (
    title,
    description,
    location,
    key_words,
    picture,
    status,
    delivery,
    created_at,
    updated_at,
    category_id,
    user_id,
    disabled
) VALUES (
    'Vest femme',
    'Vest en laine, taille M, couleur noire. Idéal pour la mi-saison.',
    'Paris, France',
    'vest, femme, hiver',
    'https://th.bing.com/th/id/OIP.QZNKF9aSbVqNtO_BzwoPGAHaHa?rs=1&pid=ImgDetMain',
    'NEUF',
    'LIVRAISON',
    '2023-12-08',
    '2023-12-08',
    1,           -- Assurez-vous que la catégorie avec l'ID 2 existe
    1,           -- Assurez-vous que l'utilisateur avec l'ID 1 existe
    false
);

INSERT INTO item (
    title,
    description,
    location,
    key_words,
    picture,
    status,
    delivery,
    created_at,
    updated_at,
    category_id,
    user_id,
    disabled
) VALUES (
    'Chassure de foot',
    'Chassure de football pour hommes, taille 42, en cuir synthétique. Idéale pour les terrains synthétiques.',
    'Marseille, France',
    'chassure, foot, homme',
    'https://www.soccerbible.com/media/63977/2-nemeziz-lone-hunter.jpg',
    'BON',
    'A_MAIN',
    '2022-11-02',
    '2022-11-02',
    1,           
    2,
    false
);

INSERT INTO item (
    title,
    description,
    location,
    key_words,
    picture,
    status,
    delivery,
    created_at,
    updated_at,
    category_id,
    user_id,
    disabled
) VALUES (
    'Pantalon femme',
    'Pantalon en coton, taille S, couleur grise. Très confortable et stylé pour toute occasion.',
    'Lyon, France',
    'pantalon, femme, vetement',
    'https://th.bing.com/th/id/OIP.TmQRwhbZt7WpXiFedLozFAHaKO?rs=1&pid=ImgDetMain',
    'ACCEPTABLE',
    'LIVRAISON',
    '2024-03-08',
    '2023-03-08',
    1,           -- Assurez-vous que la catégorie avec l'ID 2 existe
    2  ,          -- Assurez-vous que l'utilisateur avec l'ID 1 existe,
    false
);

INSERT INTO item (
    title,
    description,
    location,
    key_words,
    picture,
    status,
    delivery,
    created_at,
    updated_at,
    category_id,
    user_id,
    disabled
) VALUES (
    'Les Misérables', 
    'Édition spéciale avec couverture rigide. Le chef-d’œuvre de Victor Hugo.', 
    'Paris, France', 
    'livre, roman, classique, Victor Hugo', 
    'https://pictures.abebooks.com/inventory/30947993210_4.jpg', 
    'NEUF', 
    'LIVRAISON', 
    NOW(), 
    NOW(), 
    4,           -- category_id (Assurez-vous que la catégorie avec l'ID 2 correspond aux livres)
    1,           -- user_id (Assurez-vous que l'utilisateur avec l'ID 1 existe)
    false
);


INSERT INTO item (
    title,
    description,
    location,
    key_words,
    picture,
    status,
    delivery,
    created_at,
    updated_at,
    category_id,
    user_id,
    disabled
) VALUES (
    'Smartphone Samsung Galaxy S21', 
    'Smartphone haut de gamme, écran AMOLED 120Hz, 256 Go de stockage.', 
    'Marseille, France', 
    'smartphone, samsung, galaxy, electronique', 
    'https://cdn.storifyme.com/accounts/cashify-in-0561312/assets/f-galaxy-s21-fe-vs-galaxy-s21-2-89671686337871561.jpeg?t=1686401174000', 
    'BON', 
    'LIVRAISON', 
    NOW(), 
    NOW(), 
    2,           -- category_id (Assurez-vous que l'ID 3 correspond aux électroniques)
    2,           -- user_id (Assurez-vous que l'utilisateur avec l'ID 2 existe)
    false
);


INSERT INTO item (
    title,
    description,
    location,
    key_words,
    picture,
    status,
    delivery,
    created_at,
    updated_at,
    category_id,
    user_id,
    disabled
) VALUES (
    'Pneu Michelin 205/55 R16', 
    'Pneu de voiture en très bon état, peu utilisé. Idéal pour une voiture compacte.', 
    'Toulouse, France', 
    'pneu, automobile, michelin, voiture', 
    'https://th.bing.com/th/id/OIP.NfdQk8EVqDn-MWEtyDnJiQAAAA?w=141&h=150&c=7&r=0&o=5&dpr=1.1&pid=1.7', 
    'ACCEPTABLE', 
    'A_MAIN', 
    NOW(), 
    NOW(), 
    5,           -- category_id (Assurez-vous que l'ID 4 correspond aux automobiles)
    1,           -- user_id (Assurez-vous que l'utilisateur avec l'ID 3 existe)
    false
);





INSERT INTO request (owner_id, requester_id, item_id, status) 
VALUES (1, 2, 1, 'PENDING');

INSERT INTO item_notification (user_id, item_id, is_new) 
VALUES (1, 1, true),(2, 2, true),(2, 3, true),(1, 2, true),(2, 4, false);
