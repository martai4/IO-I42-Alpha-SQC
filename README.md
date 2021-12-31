# IO-I42-Alpha-SQC [![Java CI](https://github.com/martai4/IO-I42-Alpha-SQC/actions/workflows/ci.yml/badge.svg?branch=IIAS-35-main)](https://github.com/martai4/IO-I42-Alpha-SQC/actions/workflows/ci.yml) 

Indeksy:

143942
145298
145381
146470

### Scenario Quality Checker (SQC)

Dla analityków dokumentujących wymagania funkcjonalne za pomocą scenariuszy nasza aplikacja SQC dostarczy informacji ilościowych oraz umożliwi wykrywanie problemów w wymaganiach funkcjonalnych zapisanych w postaci scenariuszy. Aplikacja będzie dostępna poprzez GUI a także jako zdalne API dzięki czemu można ją zintegrować z istniejącymi narzędziami.

---

## Notacja scenariuszy:
- [x] Scenariusz zawiera nagłówek określający jego tytuł i aktorów (zewnętrznych oraz system)

- [x] Scenariusz składa się z kroków (każdy krok zawiera tekst)

- [x]  Kroki mogą zawierać pod-scenariusze (dowolny poziom zagłębień)

- [x]  Kroki mogą się zaczynać od słów kluczowych: IF, ELSE, FOR EACH
