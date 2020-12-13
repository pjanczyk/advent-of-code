with open('input.txt') as f:
    lines = f.read().splitlines()

start_time = int(lines[0])
buses = [int(bus) for bus in lines[1].split(',') if bus != 'x']

eariest_bus_departures = [((start_time + bus - 1) // bus) * bus for bus in buses]

eariest_bus, eariest_bus_departure = min(zip(buses, eariest_bus_departures), key=lambda x: x[1])

print(eariest_bus * (eariest_bus_departure - start_time))
