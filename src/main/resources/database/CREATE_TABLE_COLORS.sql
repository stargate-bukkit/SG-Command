CREATE TABLE IF NOT EXISTS colors {
    category NVARCHAR(8) NOT NULL,
    pointerColor NVARCHAR(8) NOT NULL,
    textColor NVARCHAR(8) NOT NULL,
    target NVARCHAR(24) NOT NULL,
    PRIMARY KEY (
        category,
        target
    )
};