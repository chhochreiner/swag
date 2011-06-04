package at.ac.tuwien.swag.webapp.in.messages;

import java.util.ArrayList;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class Outbox extends Panel {
    private static final long serialVersionUID = -4045913776508864182L;

    public Outbox(String id) {
        super(id);

        ArrayList<TODOREMOVE> outboxList = new ArrayList<TODOREMOVE>();

        for (int i = 0; i < 100; i++) {
            outboxList.add(new TODOREMOVE(i + "", ("moep" + i), "", "reciever"));
        }

        DataView<TODOREMOVE> dataView =
            new DataView<TODOREMOVE>("outboxlist", new MessageSortableDataProvider(outboxList)) {
                private static final long serialVersionUID = -7500357470052232668L;

                @Override
                protected void populateItem(Item<TODOREMOVE> item) {
                    TODOREMOVE message = item.getModelObject();
                    PageParameters param = new PageParameters();
                    param.add("id", message.getId());
                    item.add(new Label("subject", message.getSubject()));
                    item.add(new Label("reciever", message.getReciever()));
                    item.add(new BookmarkablePageLink<String>("view", MessageDetail.class, param));
                }
            };

        dataView.setItemsPerPage(15);
        add(dataView);

        add(new PagingNavigator("navigator", dataView));
    }

}
