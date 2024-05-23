package org.yezproject.pet.transaction;

import static org.yezproject.pet.RandomUtils.*;

public enum ModifyFunctionHandler {
    CATEGORY {
        @Override
        void modify(Transaction transaction) {
            transaction.modifyCategoryId(randomShortString());
        }
    },
    NAME {
        @Override
        void modify(Transaction transaction) {
            transaction.modifyName(randomShortString());
        }
    },
    AMOUNT {
        @Override
        void modify(Transaction transaction) {
            transaction.modifyAmount(randomPositiveDouble());
        }
    },
    CURRENCY {
        @Override
        void modify(Transaction transaction) {
            transaction.modifyCurrency(randomFrom(Currency.values()));
        }
    },
    TRANSACTION_DATE {
        @Override
        void modify(Transaction transaction) {
            transaction.modifyTransactionDate(randomInstant());
        }
    };

    abstract void modify(Transaction transaction);
}
