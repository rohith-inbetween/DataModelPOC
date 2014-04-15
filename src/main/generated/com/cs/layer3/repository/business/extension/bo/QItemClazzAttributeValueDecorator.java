package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QItemClazzAttributeValueDecorator is a Querydsl query type for ItemClazzAttributeValueDecorator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QItemClazzAttributeValueDecorator extends EntityPathBase<ItemClazzAttributeValueDecorator> {

    private static final long serialVersionUID = 1222683790L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemClazzAttributeValueDecorator itemClazzAttributeValueDecorator = new QItemClazzAttributeValueDecorator("itemClazzAttributeValueDecorator");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final com.cs.layer3.repository.business.defalt.bo.QAttribute attribute;

    public final com.cs.layer3.repository.business.defalt.bo.QClazz clazz;

    public final com.cs.layer3.repository.business.defalt.bo.QEntity entity;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public final QValue value;

    public QItemClazzAttributeValueDecorator(String variable) {
        this(ItemClazzAttributeValueDecorator.class, forVariable(variable), INITS);
    }

    public QItemClazzAttributeValueDecorator(Path<? extends ItemClazzAttributeValueDecorator> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemClazzAttributeValueDecorator(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemClazzAttributeValueDecorator(PathMetadata<?> metadata, PathInits inits) {
        this(ItemClazzAttributeValueDecorator.class, metadata, inits);
    }

    public QItemClazzAttributeValueDecorator(Class<? extends ItemClazzAttributeValueDecorator> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attribute = inits.isInitialized("attribute") ? new com.cs.layer3.repository.business.defalt.bo.QAttribute(forProperty("attribute")) : null;
        this.clazz = inits.isInitialized("clazz") ? new com.cs.layer3.repository.business.defalt.bo.QClazz(forProperty("clazz")) : null;
        this.entity = inits.isInitialized("entity") ? new com.cs.layer3.repository.business.defalt.bo.QEntity(forProperty("entity")) : null;
        this.value = inits.isInitialized("value") ? new QValue(forProperty("value")) : null;
    }

}

