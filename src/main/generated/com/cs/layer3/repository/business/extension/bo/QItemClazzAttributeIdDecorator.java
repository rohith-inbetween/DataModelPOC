package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QItemClazzAttributeIdDecorator is a Querydsl query type for ItemClazzAttributeIdDecorator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QItemClazzAttributeIdDecorator extends EntityPathBase<ItemClazzAttributeIdDecorator> {

    private static final long serialVersionUID = -526509796L;

    public static final QItemClazzAttributeIdDecorator itemClazzAttributeIdDecorator = new QItemClazzAttributeIdDecorator("itemClazzAttributeIdDecorator");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final NumberPath<Long> attributeId = createNumber("attributeId", Long.class);

    public final NumberPath<Long> clazzId = createNumber("clazzId", Long.class);

    public final NumberPath<Long> entityId = createNumber("entityId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public final StringPath value = createString("value");

    public QItemClazzAttributeIdDecorator(String variable) {
        super(ItemClazzAttributeIdDecorator.class, forVariable(variable));
    }

    public QItemClazzAttributeIdDecorator(Path<? extends ItemClazzAttributeIdDecorator> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemClazzAttributeIdDecorator(PathMetadata<?> metadata) {
        super(ItemClazzAttributeIdDecorator.class, metadata);
    }

}

