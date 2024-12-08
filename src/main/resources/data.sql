
INSERT INTO app_user (name, email, password, phone, address) VALUES
('Chemsou Br', 'chemsou@example.com', 'Chemsou', '0123456789', '38 rue de Clermon-Ferrand, France');

INSERT INTO category (name) VALUES
('Vêtements');

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
    user_id
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
    1            -- user_id (Assurez-vous que l'utilisateur avec l'ID 1 existe)
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
    user_id
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
    1            -- Assurez-vous que l'utilisateur avec l'ID 1 existe
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
    user_id
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
    1            
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
    user_id
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
    1            -- Assurez-vous que l'utilisateur avec l'ID 1 existe
);

