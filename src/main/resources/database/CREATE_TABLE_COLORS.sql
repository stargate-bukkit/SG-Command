CREATE TABLE IF NOT EXISTS colors (
    category NVARCHAR(8) NOT NULL,
    target NVARCHAR(24) NOT NULL,
    pointerColor NVARCHAR(8),
    textColor NVARCHAR(8),
    background NVARCHAR(16),
    PRIMARY KEY (
        category,
        target
    )
);