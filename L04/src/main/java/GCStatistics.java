import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Created by dzvyagin on 26.04.2017.
 */
public class GCStatistics {

    public void printGC() {
        long totalGarbageCollections = 0;
        long garbageCollectionTime = 0;
        StringBuilder stringBuilder = new StringBuilder();

        String format = "%25s	count: %10d	time: %10d	\n";

        stringBuilder.append("\n--------------- GC Statistic -------------------\n");

        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            String name = gc.getName();

            long count = gc.getCollectionCount();

            if (count >= 0) {
                totalGarbageCollections += count;
            }

            long time = gc.getCollectionTime();

            if (time >= 0) {
                garbageCollectionTime += time;
            }

            stringBuilder.append(String.format(format, name, count, time));
        }

        stringBuilder.append(String.format(format, "total", totalGarbageCollections, garbageCollectionTime));
        stringBuilder.append("\n------------------------------------------------\n");
        System.out.println(stringBuilder.toString());
//        LOG.debug("{}", );
    }
}
