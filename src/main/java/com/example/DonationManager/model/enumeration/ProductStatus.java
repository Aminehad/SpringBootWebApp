package com.example.DonationManager.model.enumeration;

public enum ProductStatus {
    DEFECTUEUX, // 1 - Très mauvais état, inutilisable
    USE,        // 2 - En mauvais état, mais encore partiellement utilisable
    ACCEPTABLE, // 3 - En état moyen, fonctionnel avec des défauts visibles
    BON,        // 4 - En bon état, quelques traces d'usure minimes
    NEUF        // 5 - Excellent état, comme neuf
}