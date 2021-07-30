# London, ON Waste Calendar

This program creates an iCalendar file for a provided [London, Ontario collection zone](https://apps.london.ca/ZoneFinder/).

## Usage

Using the compiled binary for macOS or Linux:

```
$ waste-calendar --help

waste-calendar outputs an iCalendar file of your London, ON
Garbage Calendar by providing your collection zone (https://apps.london.ca/ZoneFinder/).

Usage: waste-calendar [options]

Options:
  -z, --zone (A-G)  https://apps.london.ca/ZoneFinder/
  -h, --help
```

```
$ waste-calendar --zone E

Generated "Garbage Calendar.ics".

```

## License

Copyright © 2021 Brendon Walsh.

Licensed under the EPL (see the file LICENSE).
