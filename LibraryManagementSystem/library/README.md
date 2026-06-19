# Library Management System — Java

A console-based Library Management System built in plain Java (no external dependencies).

## Features
- Add / remove books with multiple copies
- Register / remove library members
- Borrow and return books (max 3 per member, 14-day loan period)
- Overdue tracking
- Search books by title or author
- Member borrow history
- Pre-loaded sample data to explore right away

## Project Structure
```
library/
└── src/
    ├── Book.java          — Book model
    ├── Member.java        — Member model
    ├── BorrowRecord.java  — Borrow/Return record model
    ├── LibraryService.java — Core business logic
    └── LibraryApp.java    — Main CLI entry point
```

## Requirements
- Java 17 or higher (uses switch expressions and text blocks)

## Compile & Run

### From the `src/` directory:
```bash
cd src
javac *.java
java LibraryApp
```

### Or from the project root:
```bash
javac -d out src/*.java
java -cp out LibraryApp
```

## Sample Data (Pre-loaded)
**Books:**
| ISBN | Title | Author | Copies |
|------|-------|--------|--------|
| 978-0-06-112008-4 | To Kill a Mockingbird | Harper Lee | 3 |
| 978-0-7432-7356-5 | 1984 | George Orwell | 2 |
| 978-0-14-028329-7 | Of Mice and Men | John Steinbeck | 2 |
| 978-0-7432-7357-2 | Animal Farm | George Orwell | 4 |

**Members:**
| ID | Name |
|----|------|
| M001 | Alice Johnson |
| M002 | Bob Smith |
| M003 | Carol White |

## Business Rules
- A member can borrow up to **3 books** at a time
- Loan period is **14 days**
- A book with borrowed copies cannot be removed
- A member with unreturned books cannot be removed
