# Java Store Application

Конзолно Java приложение, което симулира процеса на зареждане и продажба на стоки в магазин.

## ✅ Функционалности
- **Продукти** с категория (хранителни/нехранителни), доставка и срок на годност  
- **Надценки и намаления** според политика (`MarkupPolicy`)  
- **Касиери** с ID, име и заплата  
- **Каси регистри** (`CashRegister`) — стартиране на продажба, добавяне на артикули, проверка на наличности  
- **Касови бележки** (`Receipt`): сериализация в JSON, запис във файл `receipt-<номер>.txt`  
- **Глобални метрики** (`ReceiptNumberGenerator`): брой издадени бележки и общ оборот  
- **Изчисляване на разходи, приходи и печалба** в `Store`  
- **Unit тестове** (JUnit 5 + Mockito)

## 🛠 Предварителни изисквания
- Java 17+  
- Maven 3.6+

## 🚀 Как да компилираш и тестваш
```bash
# Изчистване, компилация и изпълнение на тестовете
mvn clean test

# Само компилация
mvn clean compile


# Стартирай главния примерен клас (Main.java)
mvn exec:java

# Ако искаш да пропуснеш тестовете
mvn exec:java -DskipTests

-------------------------------------------------------------------------------------------------------

📂 Структура на проекта

src/
├── main/java/bg/nbu/store/
│   ├── bootstrap/        Main.java  
│   ├── config/           MarkupPolicy.java, DefaultMarkupPolicy.java  
│   ├── domain/           Product.java, NonFoodProduct.java, FoodProduct.java, InventoryItem.java, Category.java  
│   ├── exceptions/       ExpiredProductException.java, InsufficientQuantityException.java  
│   ├── people/           Cashier.java  
│   ├── pos/              CashRegister.java  
│   ├── receipt/          Receipt.java, ReceiptLine.java, ReceiptNumberGenerator.java  
│   └── store/            Store.java  
└── test/java/bg/nbu/store/  
    └── …                Unit тестове за всички ключови класове  
pom.xml



-------------------------------------------------------------------------------------------------------
✏️ Примерен код (Работа с CashRegister)

// Подготовка на инвентара и касиер
Cashier cashier = new Cashier("C1", "Ivan", 1200.0);
NonFoodProduct widget = new NonFoodProduct("P1", "Widget", 100.0, LocalDate.now().plusDays(10), 50);
InventoryItem item = new InventoryItem(widget);
List<InventoryItem> inventory = List.of(item);

// Създаване на каса
MarkupPolicy policy = new DefaultMarkupPolicy();
CashRegister register = new CashRegister("REG1", inventory, policy);
register.assignCashier(cashier.getId());

// Стартиране на продажба
register.startSale();
register.addItem(widget, 2);

// Изчисляване на точната сума
BigDecimal total = register.getCurrentLines().stream()
    .map(line -> line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())))
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Приключване на продажбата и генериране на бележка
Receipt receipt = register.finishSale(total);
System.out.println(receipt);

---------------------------------------------------------------------------------------------------------------------
📊 Глобални метрики
Брой издадени бележки:
int count = ReceiptNumberGenerator.getCount();

Общ оборот:
BigDecimal turnover = ReceiptNumberGenerator.getTurnover();

---------------------------------------------------------------------------------------------------------------------

🏪 Магазин (Store)

Класът Store събира информация за:

работещите касиери

наличния инвентар

издадените касови бележки

И предоставя методи за:

totalSalaryCosts() – общо заплати

totalDeliveryCosts() – доставка на стоки

totalExpenses() – общи разходи

totalRevenue() – общи приходи

profit() – печалба

