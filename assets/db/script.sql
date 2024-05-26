-- Users table
CREATE TABLE Users (
    userId INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    fullName TEXT,
    isBlocked INTEGER DEFAULT 0
);

-- Roles table
CREATE TABLE Roles (
    roleId INTEGER PRIMARY KEY AUTOINCREMENT,
    roleName TEXT NOT NULL UNIQUE
);

-- UserRoles table
CREATE TABLE UserRoles (
    userId INTEGER,
    roleId INTEGER,
    PRIMARY KEY (userId, roleId),
    FOREIGN KEY (userId) REFERENCES Users(userId),
    FOREIGN KEY (roleId) REFERENCES Roles(roleId)
);

-- Media table
CREATE TABLE Media (
    mediaId INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    category TEXT NOT NULL,
    value NUMERIC(10, 2) NOT NULL,
    currentPrice NUMERIC(10, 2) NOT NULL CHECK (currentPrice BETWEEN value * 0.3 AND value * 1.5),
    vatRate NUMERIC(3, 2) DEFAULT 0.10,
    imageUrl TEXT NOT NULL,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE Books (
    mediaId INTEGER PRIMARY KEY,
    authors TEXT,
    coverType TEXT,
    publisher TEXT,
    publicationDate DATE,
    pages INTEGER,
    language TEXT,
    genre TEXT,
    FOREIGN KEY (mediaId) REFERENCES Media(mediaId)
);

-- CDs table
CREATE TABLE CDsLPs (
    mediaId INTEGER PRIMARY KEY,
    artists TEXT,
    recordLabel TEXT,
    tracklist TEXT,
    genre TEXT,
    releaseDate DATE,
    type INTEGER,
    FOREIGN KEY (mediaId) REFERENCES Media(mediaId)
);

-- DVDs table
CREATE TABLE DVDs (
    mediaId INTEGER PRIMARY KEY,
    discType TEXT,
    director TEXT,
    runtime INTEGER,
    studio TEXT,
    language TEXT,
    subtitles TEXT,
    genre TEXT,
    releaseDate DATE,
    FOREIGN KEY (mediaId) REFERENCES Media(mediaId)
);

-- Inventory table
CREATE TABLE Inventory (
    inventoryId INTEGER PRIMARY KEY AUTOINCREMENT,
    mediaId INTEGER,
    barcode TEXT UNIQUE NOT NULL,
    description TEXT,
    quantity INTEGER NOT NULL,
    warehouseEntryDate DATE,
    dimensions TEXT,
    weight NUMERIC(10, 2),
    FOREIGN KEY (mediaId) REFERENCES Media(mediaId)
);

-- PriceHistory table
CREATE TABLE PriceHistory (
    priceHistoryId INTEGER PRIMARY KEY AUTOINCREMENT,
    mediaId INTEGER,
    oldPrice NUMERIC(10, 2) NOT NULL,
    newPrice NUMERIC(10, 2) NOT NULL,
    changeDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mediaId) REFERENCES Media(mediaId)
);

-- Orders table
CREATE TABLE Orders (
    orderId INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER,
    customerName TEXT NOT NULL,
    customerEmail TEXT NOT NULL,
    customerPhone TEXT,
    deliveryProvince TEXT,
    deliveryAddress TEXT,
    orderStatus TEXT DEFAULT 'Pending',
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(userId)
);

-- OrderItems table
CREATE TABLE OrderItems (
    orderItemId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER,
    mediaId INTEGER,
    quantity INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (orderId) REFERENCES Orders(orderId),
    FOREIGN KEY (mediaId) REFERENCES Media(mediaId)
);

-- Cart table
CREATE TABLE Cart (
    cartId INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER,
    sessionId TEXT UNIQUE NOT NULL,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(userId)
);

-- CartItems table
CREATE TABLE CartItems (
    cartItemId INTEGER PRIMARY KEY AUTOINCREMENT,
    cartId INTEGER,
    mediaId INTEGER,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (cartId) REFERENCES Cart(cartId),
    FOREIGN KEY (mediaId) REFERENCES Media(mediaId)
);

-- DeliveryInfo table
CREATE TABLE DeliveryInfo (
    deliveryInfoId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER,
    deliveryFee NUMERIC(10, 2) NOT NULL,
    rushOrder INTEGER DEFAULT 0,
    FOREIGN KEY (orderId) REFERENCES Orders(orderId)
);

-- TransactionHistory table
CREATE TABLE TransactionHistory (
    transactionId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER,
    deductedAmount NUMERIC(10, 2) NOT NULL,
    transactionDetails TEXT,
    transactionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    transactionStatus TEXT,
    paymentMethod TEXT,
    FOREIGN KEY (orderId) REFERENCES Orders(orderId)
);
